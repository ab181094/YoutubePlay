package com.example.youtubeplay

import android.os.Parcel
import android.os.Parcelable

class YoutubeThumbnail() : Parcelable {
    var videoID: String? = ""
    var thumbnailURL: String? = ""

    constructor(parcel: Parcel) : this() {
        videoID = parcel.readString()
        thumbnailURL = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(videoID)
        parcel.writeString(thumbnailURL)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<YoutubeThumbnail> {
        override fun createFromParcel(parcel: Parcel): YoutubeThumbnail {
            return YoutubeThumbnail(parcel)
        }

        override fun newArray(size: Int): Array<YoutubeThumbnail?> {
            return arrayOfNulls(size)
        }
    }
}