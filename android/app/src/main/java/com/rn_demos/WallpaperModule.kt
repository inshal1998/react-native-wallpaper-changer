package com.rn_demos

import android.app.WallpaperManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.facebook.react.bridge.*
import java.io.File
import java.net.URI

class WallpaperModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "WallpaperModule"
    }

    @ReactMethod
    fun setWallpaper(imagePath: String, promise: Promise) {
        try {
            val bitmap = when {
                imagePath.startsWith("content://") -> {
                    val uri = Uri.parse(imagePath)
                    val stream = reactApplicationContext.contentResolver.openInputStream(uri)
                    if (stream == null) {
                        promise.reject("FILE_NOT_FOUND", "Unable to open image stream")
                        return
                    }
                    BitmapFactory.decodeStream(stream)
                }

                imagePath.startsWith("file://") -> {
                    val file = File(URI(imagePath))
                    if (!file.exists()) {
                        promise.reject("FILE_NOT_FOUND", "Image file not found at ${file.absolutePath}")
                        return
                    }
                    BitmapFactory.decodeFile(file.absolutePath)
                }

                else -> {
                    val file = File(imagePath)
                    if (!file.exists()) {
                        promise.reject("FILE_NOT_FOUND", "Image file not found at $imagePath")
                        return
                    }
                    BitmapFactory.decodeFile(file.absolutePath)
                }
            }

            if (bitmap == null) {
                promise.reject("BITMAP_ERROR", "Failed to decode image")
                return
            }

            val wallpaperManager = WallpaperManager.getInstance(reactApplicationContext)
            wallpaperManager.setBitmap(bitmap)
            promise.resolve("Wallpaper successfully set")
        } catch (e: Exception) {
            promise.reject("WALLPAPER_ERROR", e.message)
        }
    }
}
