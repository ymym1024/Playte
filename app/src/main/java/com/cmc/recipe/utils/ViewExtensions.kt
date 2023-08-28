package com.cmc.recipe.utils

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cmc.recipe.R
import com.cmc.recipe.presentation.ui.common.CustomBottomSheetFragment
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

fun ImageView.compressAndSetImage() {
    // Get the original drawable from ImageView
    val drawable = this.drawable ?: return
    val bitmap = drawableToBitmap(drawable)

    val screenWidth = resources.displayMetrics.widthPixels
    val targetWidth = screenWidth / 2 // 50% 압축

    val originalWidth = bitmap.width
    val originalHeight = bitmap.height
    val scaleFactor = originalWidth.toFloat() / targetWidth.toFloat()
    val targetHeight = (originalHeight / scaleFactor).toInt()

    val compressedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
    setImageBitmap(compressedBitmap)
}
private fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
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

fun String.parseDateTime(): Date? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSS", Locale.getDefault())
    return try {
        dateFormat.parse(this)
    } catch (e: Exception) {
        null
    }
}

fun Date.formatDateRelativeToNow(): String {
    val currentDate = Date()
    val timeDifferenceInSeconds = ((currentDate.time - this.time) / 1000).toInt()

    return when {
        timeDifferenceInSeconds < 60 -> "${timeDifferenceInSeconds}초 전"
        timeDifferenceInSeconds < 3600 -> "${timeDifferenceInSeconds / 60}분 전"
        timeDifferenceInSeconds < 86400 -> "${timeDifferenceInSeconds / 3600}시간 전"
        timeDifferenceInSeconds < 2592000 -> "${timeDifferenceInSeconds / 86400}일 전"
        else -> SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(this)
    }
}

fun Context.highlightText(full: String, highlight: String): CharSequence {
    val color = ContextCompat.getColor(this, R.color.primary_color)

    val spannableString = SpannableString(full)
    val startIndex = full.indexOf(highlight)

    if (startIndex != -1) {
        val colorSpan = ForegroundColorSpan(color)
        spannableString.setSpan(
            colorSpan,
            startIndex,
            startIndex + highlight.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannableString
}