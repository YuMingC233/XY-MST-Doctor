package com.skydoves.chatgpt.feature.chat.worker

import com.skydoves.chatgpt.core.network.BuildConfig
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.io.File

class ClientUploadUtils {
  fun uploadImage(APIurl: String, filePath: String,fileName: String) :String? {
    // upload image
    val client = OkHttpClient()
    val requestBody = MultipartBody.Builder()
      .setType(MultipartBody.FORM)
      .addFormDataPart("source", fileName,
        File(filePath).readBytes()
          .toRequestBody("multipart/form-data".toMediaTypeOrNull(), 0, File(filePath).readBytes().size)
      )
      .build()
    val request = Request.Builder()
      .addHeader("X-API-Key", BuildConfig.CHEVERETO_KEY)
      .addHeader("format", "json")
      .url(APIurl)
      .post(requestBody)
      .build()

    val response = client.newCall(request).execute()

    if(response.isSuccessful) {
      return response.body?.string()
    } else {
      throw Exception("Failed to upload image")
    }
  }
}