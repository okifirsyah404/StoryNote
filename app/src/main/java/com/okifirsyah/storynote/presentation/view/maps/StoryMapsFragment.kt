package com.okifirsyah.storynote.presentation.view.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.okifirsyah.data.network.ApiResponse
import com.okifirsyah.storynote.R
import com.okifirsyah.storynote.base.BaseFragment
import com.okifirsyah.storynote.databinding.FragmentStoryMapsBinding
import com.okifirsyah.storynote.utils.extension.addMultipleMarker
import com.okifirsyah.storynote.utils.extension.boundsCameraToMarkers
import com.okifirsyah.storynote.utils.extension.convertToLatLng
import com.okifirsyah.storynote.utils.extension.setCustomStyle
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoryMapsFragment : BaseFragment<FragmentStoryMapsBinding>() {

    private val viewModel: StoryMapsViewModel by viewModel()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentStoryMapsBinding {
        return FragmentStoryMapsBinding.inflate(inflater, container, false)
    }

    override fun initAppBar() {
        binding.toolbar.mainToolbar.apply {
            title = getString(R.string.stories_map)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    override fun initUI() {
        val mapFragment =
            childFragmentManager.findFragmentById(binding.mapContainer.id) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->

            googleMap.setCustomStyle(requireContext())
            googleMap.uiSettings.isZoomControlsEnabled = true

            viewModel.getStories()

            viewModel.storiesResult.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Error -> {
                        Timber.e("Error: Data on map")
                    }

                    is ApiResponse.Success -> {
                        googleMap.addMultipleMarker(response.data)
                        val listLocations = response.data.convertToLatLng()
                        if (listLocations.isNotEmpty()) {
                            googleMap.boundsCameraToMarkers(listLocations)
                        }
                        setButtonViews(googleMap, listLocations)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setButtonViews(googleMap: GoogleMap, listLocation: List<LatLng>) {
        binding.btnBounds.setOnClickListener {
            if (listLocation.isNotEmpty()) {
                googleMap.boundsCameraToMarkers(listLocation)
            }
        }
    }

}