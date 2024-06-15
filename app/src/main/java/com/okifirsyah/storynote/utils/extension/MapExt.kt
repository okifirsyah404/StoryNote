package com.okifirsyah.storynote.utils.extension

import android.content.Context
import android.content.res.Resources
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.okifirsyah.data.dto.StoryDto
import com.okifirsyah.storynote.R
import timber.log.Timber

fun GoogleMap.setCustomStyle(context: Context) {
    try {
        val success =
            this.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        if (!success) {
            Timber.e("Style parsing failed.")
        }
    } catch (exception: Resources.NotFoundException) {
        Timber.e("Can't find style. Error: ", exception)
    }
}

fun GoogleMap.addMultipleMarker(stories: List<StoryDto>) {
    this.clear()

    for (story in stories) {
        if (story.latitude != null && story.longitude != null) {
            val coordinate = LatLng(story.latitude!!.toDouble(), story.longitude!!.toDouble());
            val marker = this.addMarker(
                createMarkerOptions(
                    coordinate,
                    story.name
                )
            )
            marker?.tag = story
            marker?.showInfoWindow()
        }
    }
}

fun GoogleMap.addMarker(coordinate: LatLng) {
    this.clear()

    val marker = this.addMarker(
        MarkerOptions()
            .position(coordinate)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
    )
    marker?.showInfoWindow()
}

fun GoogleMap.boundsCameraToMarkers(locations: List<LatLng>) {
    val builder = LatLngBounds.builder()
    for (location in locations) {
        builder.include(location)
    }
    val bounds = builder.build()
    val zoomLevel = 15
    val cu = CameraUpdateFactory.newLatLngBounds(bounds, zoomLevel)
    this.animateCamera(cu, 1000, null)
}

fun GoogleMap.boundsCameraToMarker(location: LatLng) {
    val zoomLevel = 15F
    val cu = CameraUpdateFactory.newLatLngZoom(location, zoomLevel)
    this.animateCamera(cu, 1000, null)
}

fun List<StoryDto>.convertToLatLng(): List<LatLng> {
    val listMarker = ArrayList<LatLng>()
    for (story in this) {
        if (story.latitude != null && story.longitude != null) {
            listMarker.add(LatLng(story.latitude!!.toDouble(), story.longitude!!.toDouble()))
        }
    }
    return listMarker
}

private fun createMarkerOptions(
    location: LatLng,
    markerName: String
): MarkerOptions {
    val iconDrawable = R.drawable.ic_marker
    val icon = BitmapDescriptorFactory.fromResource(iconDrawable)
    return MarkerOptions().position(location)
        .title(markerName)
        .icon(icon)
}