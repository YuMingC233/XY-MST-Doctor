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

package com.skydoves.chatgpt.core.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GPTMessage(
  @field:Json(name = "role") val role: String,
  @field:Json(name = "content") val content: List<Content>
)


@JsonClass(generateAdapter = true)
data class Content(
  @Json(name = "type") val type: String,
  @Json(name = "text") val text: String? = null,
  @Json(name = "image_url") val imageUrl: ImageUrl? = null
)

@JsonClass(generateAdapter = true)
data class ImageUrl(
  @Json(name = "url") val url: String,
  @Json(name = "detail") val detail: String
)