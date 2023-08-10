package com.cmc.recipe.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat

fun ImageView.loadImagesWithGlide(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.loadImagesWithGlideRound(url: String,radius:Int) {

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
