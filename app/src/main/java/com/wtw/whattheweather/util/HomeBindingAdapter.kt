package com.wtw.whattheweather.util

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.wtw.whattheweather.R

object HomeBindingAdapter {

    @JvmStatic
    @BindingAdapter("set_feed_img")
    fun setFeedImg(imageView: ImageView, url : String) {

        try {
            imageView.load(url)

        }
        catch (e: Exception) {

            imageView.load(R.drawable.my_page_icon)
            Log.e("feed_img_error",e.message.toString())

        }



    }

}