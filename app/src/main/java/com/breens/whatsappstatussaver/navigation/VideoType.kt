package com.breens.whatsappstatussaver.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson

class VideoType : NavType<Video>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): Video? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Video {
        return Gson().fromJson(value, Video::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Video) {
        bundle.putParcelable(key, value)
    }
}