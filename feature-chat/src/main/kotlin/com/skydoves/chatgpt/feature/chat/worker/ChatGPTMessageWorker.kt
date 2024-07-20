/*
 * Designed and developed by 2024 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.chatgpt.feature.chat.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import cn.hutool.json.JSONUtil
import com.skydoves.chatgpt.core.data.repository.GPTMessageRepository
import com.skydoves.chatgpt.core.model.Content
import com.skydoves.chatgpt.core.model.GPTMessage
import com.skydoves.chatgpt.core.model.ImageUrl
import com.skydoves.chatgpt.core.model.network.GPTChatRequest
import com.skydoves.chatgpt.feature.chat.di.ChatEntryPoint
import com.skydoves.sandwich.getOrThrow
import com.skydoves.sandwich.isSuccess
import com.skydoves.sandwich.messageOrNull
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Message
import io.getstream.log.streamLog
import java.io.File
import javax.inject.Inject

@HiltWorker
internal class ChatGPTMessageWorker @AssistedInject constructor(
  @Assisted private val context: Context,
  @Assisted private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

  @Inject
  internal lateinit var repository: GPTMessageRepository

  @Inject
  internal lateinit var chatClient: ChatClient

  override suspend fun doWork(): Result {
    ChatEntryPoint.resolve(context).inject(this)

    val text = workerParams.inputData.getString(DATA_TEXT) ?: return Result.failure()
    val channelId = workerParams.inputData.getString(DATA_CHANNEL_ID) ?: return Result.failure()
    val lastMessage = workerParams.inputData.getString(DATA_LAST_MESSAGE)
    val ImageURLlist = workerParams.inputData.getStringArray(DATA_IMAGES)

    val messages: MutableList<GPTMessage> = mutableListOf()
    if (lastMessage != null) {
      messages.add(
        GPTMessage(
          role = "system",
          content = listOf(
            Content(
              type = "text",
              text = lastMessage
            )
          )
        )
      )
    }

    if (ImageURLlist != null && ImageURLlist[0] != null) {
      val imgUploadedList = mutableListOf<String>()
      ImageURLlist.forEach {
        val fileName = it.substringAfterLast("/")
        val filePath = it
        val url = "https://ibed.cws-aizc.online/api/1/upload"
        val response = ClientUploadUtils().uploadImage(url, filePath, fileName)
        val jo = JSONUtil.parseObj(response)
        val fileInfo = JSONUtil.parseObj(jo.getStr("image")).getJSONObject("image")
        imgUploadedList.add(fileInfo.getStr("url"))
      }
//      val imgBase64 = mutableListOf<String>()
//      ImageURLlist.forEach {
//        val file = File(it)
//        val base64Str = Base64.encodeToString(file.readBytes(),Base64.URL_SAFE or Base64.NO_WRAP)
//        // 获取文件后缀并替换url头
//        val suffix = it.substringAfterLast(".")
//        imgBase64.add("data:image/$suffix;base64,$base64Str")
//      }
      val contentList = mutableListOf<Content>().apply {
        add(Content(type = "text", text = text))
        imgUploadedList.forEach { url ->
          add(Content(type = "image_url", imageUrl = ImageUrl(url,"low")))
        }
      }
//      val contentList = mutableListOf(Content(type = "image_url", imageUrl = ImageUrl(resp,"low")))
      messages.add(GPTMessage(role = "user", content = contentList))
    } else {
      messages.add(
        GPTMessage(
          role = "user",
          content = listOf(
            Content(
              type = "text",
              text = text
          )
          )
        )
      )
    }

    val request = GPTChatRequest(
      model = "gpt-4o-mini",
      messages = messages
    )
    val response = repository.sendMessage(request)
    return if (response.isSuccess) {
      val data = response.getOrThrow()
      val messageText = data.choices.firstOrNull()?.message?.content.orEmpty()
      val messageId = data.id
      sendStreamMessage(messageText, messageId, channelId)
      streamLog { "worker success!" }
      Result.success(
        Data.Builder()
          .putString(DATA_SUCCESS, messageText)
          .putString(DATA_MESSAGE_ID, messageId)
          .build()
      )
    } else {
      streamLog { "worker failure!" }
      Result.failure(Data.Builder().putString(DATA_FAILURE, response.messageOrNull ?: "").build())
    }
  }

  private suspend fun sendStreamMessage(
    text: String,
    messageId: String,
    channelId: String
  ) {
    val channelClient = chatClient.channel(channelId)
    channelClient.sendMessage(
      message = Message(
        id = messageId,
        cid = channelClient.cid,
        text = text,
        extraData = mutableMapOf(
          MESSAGE_EXTRA_CHAT_GPT to true
        )
      )
    ).await()
  }

  companion object {
    const val DATA_TEXT = "DATA_TEXT"
    const val DATA_IMAGES = "DATA_IMAGES"
    const val DATA_CHANNEL_ID = "DATA_CHANNEL_ID"
    const val DATA_MESSAGE_ID = "DATA_PARENT_ID"
    const val DATA_LAST_MESSAGE = "DATA_LAST_MESSAGE"
    const val DATA_SUCCESS = "DATA_SUCCESS"
    const val DATA_FAILURE = "DATA_FAILURE"

    const val MESSAGE_EXTRA_CHAT_GPT = "ChatGPT"
  }
}
