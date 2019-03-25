package com.bmacedo.hirememusic.util

import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bmacedo.hirememusic.R
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(view.context).load(url).into(view)
    } else {
        view.setImageDrawable(
            ResourcesCompat.getDrawable(
                view.context.resources,
                R.drawable.logo,
                null
            )
        )
    }
}

