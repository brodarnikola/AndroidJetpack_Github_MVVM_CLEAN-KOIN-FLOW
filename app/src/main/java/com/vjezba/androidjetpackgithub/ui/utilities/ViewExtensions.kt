package com.vjezba.androidjetpackgithub.ui.utilities

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.setTitle(title: String) {
    (activity as AppCompatActivity).supportActionBar!!.title = title
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}


fun scaleDown(
    realImage: Bitmap, maxImageSize: Float,
    filter: Boolean
): Bitmap? {
    try {
        val ratio = Math.min(
            maxImageSize / realImage.width,
            maxImageSize / realImage.height
        )
        val width = Math.round(ratio * realImage.width)
        val height = Math.round(ratio * realImage.height)
        return Bitmap.createScaledBitmap(
            realImage, width,
            height, filter
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun screenWidth(context: Activity) : Int {
    // I need to find here screen width. Later I will use this screen width for place holders on home screen
    val displaymetrics = DisplayMetrics()
    context.windowManager.defaultDisplay.getMetrics(displaymetrics)
    return displaymetrics.widthPixels
}

fun returnToPreviousImage(context: Activity) : Float {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
        val display: Display = context.windowManager.defaultDisplay
        val width = display.width
        val height = display.height
        val previousImage = (width * 0.0).toFloat() // return to previous image to -50% of screen
        return previousImage
    } else {
        val display: Display = context.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        val previousImage = (width * 0.0).toFloat() // return to previous image to -50% of screen
        return previousImage
    }
}

