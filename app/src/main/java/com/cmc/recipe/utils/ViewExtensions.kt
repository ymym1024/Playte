package com.cmc.recipe.utils

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.TypedValue
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

fun ImageView.loadImagesWithGlide(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.loadImagesWithGlideRound(url: String?,radius:Int) {

    Glide.with(this)
        .load(url)
        .transform(CenterCrop(), RoundedCorners(radius))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)

}

fun ImageView.bitmapImagesWithGlideRound(url: Bitmap?, radius:Int) {

    Glide.with(this)
        .load(url)
        .transform(CenterCrop(), RoundedCorners(radius))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)

}

fun String.convertTimestampToDate(time: Long){
   SimpleDateFormat("yy.MM.dd").format(time).toString()
}

fun dpToPx(context: Context, dp: Float): Int {
    val displayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics).toInt()
}

fun Long.convertLongToTime(durationMillis: Long): String {
    val seconds = (durationMillis / 1000).toInt()
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60

    return String.format("%02d:%02d", minutes, remainingSeconds)
}

fun Context.statusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
    else 0
}

fun Context.navigationHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")

    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
    else 0
}

fun Activity.setStatusBarTransparent() {
    window.apply {
        setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
    if(Build.VERSION.SDK_INT >= 30) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

}

fun Activity.setStatusBarOrigin() {
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    if(Build.VERSION.SDK_INT >= 30) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
    }
}

fun Context.getRealPathFromURI(uri: Uri): String {
    var columnIndex = 0
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor? = contentResolver.query(uri, proj, null, null, null)
    if (cursor?.moveToFirst() == true) {
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    }
    val result = cursor?.getString(columnIndex) ?: ""
    cursor?.close()
    return result
}

fun ImageView.resizeBitmapToSquare(size:Int){
    val pixel = dpToPx(context,size.toFloat())
    this.layoutParams.width = pixel
    this.layoutParams.height = pixel
}

fun String.parseAndFormatDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

    val date = inputFormat.parse(this)
    return if (date != null) {
        outputFormat.format(date)
    } else {
        "Invalid Date"
    }
}