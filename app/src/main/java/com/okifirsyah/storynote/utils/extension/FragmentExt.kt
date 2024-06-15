package com.okifirsyah.storynote.utils.extension

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.okifirsyah.storynote.R
import com.okifirsyah.storynote.utils.helper.MutableReference
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume

fun Fragment.showSingleActionDialog(
    title: String,
    message: String,
    onTapLabel: String = getString(R.string.ok),
    onTap: (() -> Unit)? = null
) {
    MaterialAlertDialogBuilder(requireActivity()).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(onTapLabel) { p0, _ ->
            onTap?.invoke() ?: p0.dismiss()
        }
        setOnDismissListener {
            it.dismiss()
        }
    }.show()
}

fun Fragment.showDecisionDialog(
    title: String,
    message: String,
    onYesLabel: String = getString(R.string.yes),
    onNoLabel: String = getString(R.string.no),
    onYes: (() -> Unit)? = null,
    onNo: (() -> Unit)? = null
) {
    MaterialAlertDialogBuilder(requireActivity()).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(onYesLabel) { p0, _ ->
            onYes?.invoke() ?: p0.dismiss()
        }
        setNegativeButton(onNoLabel) { p0, _ ->
            onNo?.invoke() ?: p0.dismiss()
        }
        setOnDismissListener {
            it.dismiss()
        }
    }.show()
}

fun Fragment.showLoadingDialog(
    title: String = "Loading",
    message: String = "Please wait...",
    loading: Boolean,
    dialogReference: MutableReference<AlertDialog?>
) {
    if (loading) {
        if (dialogReference.value == null) {
            val dialogView = layoutInflater.inflate(R.layout.dialog_progress, null)
            dialogView.findViewById<TextView>(R.id.progress_message).text = message

            val builder = MaterialAlertDialogBuilder(requireActivity()).apply {
                setTitle(title)
                setView(dialogView)
                setCancelable(false)
            }
            dialogReference.value = builder.create()
            dialogReference.value?.show()
        }
    } else {
        dialogReference.value?.dismiss()
        dialogReference.value = null
    }
}

suspend fun Context.getSubAdminAreaName(latitude: Double, longitude: Double): String? {
    val geocoder = Geocoder(this)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        suspendCancellableCoroutine { continuation ->
            geocoder.getFromLocation(
                latitude,
                longitude,
                1
            ) { addresses ->
                if (addresses.isNotEmpty()) {
                    val subAdminArea = addresses[0].subAdminArea
                    Timber.tag("ContextNew").d("Locality: $addresses")
                    Timber.tag("ContextNew").d("Address: ${addresses[0]}")
                    Timber.tag("ContextNew").d("Locality2: ${addresses[0].locality}")
                    Timber.tag("ContextNew").d("Locality3: ${addresses[0].adminArea}")
                    Timber.tag("ContextNew").d("Locality4: ${addresses[0].subAdminArea}")
                    continuation.resume(subAdminArea)
                } else {
                    continuation.resume(null)
                }
            }
        }
    } else {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val subAdminArea = addresses?.get(0)?.subAdminArea
        Timber.tag("ContextOLD").d("Locality: $subAdminArea")
        subAdminArea
    }
}
