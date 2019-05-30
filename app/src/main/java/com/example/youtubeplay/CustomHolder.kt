package com.example.youtubeplay

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView

class CustomHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val thumbnails = itemView.findViewById<ImageView>(R.id.thumbnail)
}