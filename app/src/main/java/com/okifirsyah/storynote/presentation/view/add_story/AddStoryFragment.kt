package com.okifirsyah.storynote.presentation.view.add_story

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.okifirsyah.domain.utils.extensions.observeResult
import com.okifirsyah.storynote.R
import com.okifirsyah.storynote.base.BaseFragment
import com.okifirsyah.storynote.databinding.FragmentAddStoryBinding
import com.okifirsyah.storynote.utils.extension.addMarker
import com.okifirsyah.storynote.utils.extension.boundsCameraToMarker
import com.okifirsyah.storynote.utils.extension.gone
import com.okifirsyah.storynote.utils.extension.setCustomStyle
import com.okifirsyah.storynote.utils.extension.show
import com.okifirsyah.storynote.utils.extension.showLoadingDialog
import com.okifirsyah.storynote.utils.extension.showSingleActionDialog
import com.okifirsyah.storynote.utils.helper.MutableReference
import com.okifirsyah.storynote.utils.helper.getImageUri
import com.okifirsyah.storynote.utils.helper.uriToFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class AddStoryFragment : BaseFragment<FragmentAddStoryBinding>() {

    private val viewModel: AddStoryViewModel by viewModel()
    private val loadingDialogReference = MutableReference<AlertDialog?>(null)

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val cancellationTokenSource = CancellationTokenSource()

    private var coordinates: LatLng? = null
    private var imageUri: Uri? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAddStoryBinding {
        return FragmentAddStoryBinding.inflate(inflater, container, false)
    }

    override fun initAppBar() {
        binding.toolbar.mainToolbar.apply {
            title = getString(R.string.add_story)
            setNavigationOnClickListener {
//                findNavController().previousBackStackEntry?.savedStateHandle?.set(
//                    DashboardFragment.SHOULD_REFRESH,
//                    true
//                )
                findNavController().popBackStack()
            }
        }
    }

    override fun initUI() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions.entries.all { it.value }

            if (!granted) {
                MaterialAlertDialogBuilder(requireActivity()).apply {
                    setTitle(getString(R.string.title_permission_required))
                    setMessage(getString(R.string.desc_permission_required))
                    setPositiveButton(getString(R.string.ok)) { _, _ ->
                        intentToAppSetting()
                    }
                }.show()
            }
        }

        launchMediaPermission()
        launchLocationPermission()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment =
            childFragmentManager.findFragmentById(binding.mapContainer.id) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            googleMap.setCustomStyle(requireContext())
            googleMap.uiSettings.isZoomControlsEnabled = true
            if (checkLocationPermission()) {
                googleMap.isMyLocationEnabled = true
            } else {
                launchLocationPermission()
            }

            getPosition(
                onSuccess = { location ->
                    coordinates = LatLng(location.latitude, location.longitude)
                    googleMap.addMarker(coordinates!!)
                    googleMap.boundsCameraToMarker(coordinates!!)

                },
                onFailure = { exception ->
                    Timber.e(exception)
                }
            )

            googleMap.setOnMapClickListener { latLng ->
                binding.apply {
                    googleMap.addMarker(latLng)
                    googleMap.boundsCameraToMarker(latLng)
                    coordinates = latLng
                }
            }
        }

    }

    override fun initActions() {
        binding.apply {
            switchShareLocation.setOnCheckedChangeListener() { _, isChecked ->
                if (isChecked) {
                    if (checkLocationPermission()) {
                        mapContainer.show()
                    } else {
                        launchLocationPermission()
                    }
                } else {
                    binding.mapContainer.gone()
                }
            }
        }
    }

    override fun initProcess() {
        binding.apply {
            imgStoryHolder.setOnClickListener {
                if (checkMediaPermission()) {
                    showImagePickerMenu()
                } else {
                    launchMediaPermission()
                }
            }

            buttonAdd.setOnClickListener {
                val description = binding.edAddDescription.text.toString()

                if (imageUri == null) {
                    MaterialAlertDialogBuilder(requireActivity()).apply {
                        setTitle(getString(R.string.title_dialog_image))
                        setMessage(getString(R.string.msg_dialog_image))
                        setPositiveButton(getString(R.string.ok)) { _, _ -> }
                    }.show()

                    return@setOnClickListener
                }

                if (description.isEmpty()) {
                    MaterialAlertDialogBuilder(requireActivity()).apply {
                        setTitle(getString(R.string.title_dialog_desc)).setMessage(getString(R.string.msg_dialog_desc))
                            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
                    }.show()

                    return@setOnClickListener
                }

                viewModel.addStory(
                    image = uriToFile(requireActivity(), imageUri!!),
                    description = description,
                    latitude = coordinates?.latitude?.toFloat(),
                    longitude = coordinates?.longitude?.toFloat()
                )
            }
        }
    }

    override fun initObservers() {
        viewModel.addStoryResult.observeResult(viewLifecycleOwner) {
            onLoading = {
                showError(false, "")
                showLoading(true)
            }
            onSuccess = { _ ->
                showLoading(false)
                showError(false, "")
                findNavController().popBackStack()
            }
            onError = { message ->
                showLoading(false)
                showError(true, message)
            }
        }
    }

    override fun showLoading(isLoading: Boolean) {
        showLoadingDialog(loading = isLoading, dialogReference = loadingDialogReference)
    }

    override fun showError(isError: Boolean, message: String) {
        if (isError) {
            showSingleActionDialog(
                title = "Error",
                message = message,
            )
        }
    }

    override fun onClose() {
        super.onClose()
        loadingDialogReference.value = null
    }

    private fun launchLocationPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun checkLocationPermission(): Boolean {
        return requireContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                requireContext().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun getPosition(onSuccess: (Location) -> Unit, onFailure: (Exception) -> Unit) {
        if (checkLocationPermission()) {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                onSuccess(location)
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
        } else {
            launchLocationPermission()
        }
    }

    private fun launchMediaPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.CAMERA
            )
        )
    }

    private fun checkMediaPermission(): Boolean {
        return requireContext().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun intentToAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.setData(uri)
        startActivity(intent)
    }

    private fun showImagePickerMenu() {
        MaterialAlertDialogBuilder(requireActivity()).apply {
            setItems(R.array.pictures) { _, p1 ->
                if (p1 == 0) {
                    launchCamera()
                } else {
                    launchGallery()
                }
            }
        }.show()
    }

    private fun launchCamera() {
        imageUri = getImageUri(requireActivity())
        cameraLauncher.launch(imageUri)
    }

    private fun launchGallery() {
        pickFileImage.launch("image/*")
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                setImage()
            }
        }

    private val pickFileImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                setImage()
            }
        }

    private fun setImage() {
        binding.imgStoryHolder.setImageURI(imageUri)
    }
}