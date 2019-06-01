package com.example.youtubeplay

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

class CustomAdapter(
    val mainActivity: MainActivity,
    val hashMap: LinkedHashMap<Int, YoutubeThumbnail>
) : RecyclerView.Adapter<CustomHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomHolder {
        val inflater = LayoutInflater.from(p0.context)
        return CustomHolder(inflater.inflate(R.layout.custom_view, p0, false))
    }

    override fun getItemCount(): Int {
        return hashMap.size
    }

    override fun onBindViewHolder(p0: CustomHolder, p1: Int) {
        val youtubeThumbnail = hashMap[p1]
        Glide.with(mainActivity)
            .asBitmap()
            .load(youtubeThumbnail!!.thumbnailURL)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                    p0.thumbnails.setImageBitmap(resource)
                }

            })

        p0.itemView.setOnClickListener {
            mainActivity.setItemVideo(p1)
        }
    }
}