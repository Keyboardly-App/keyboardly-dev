package app.keyboardly.local.model

import java.io.Serializable

/**
 * Created by Zainal on 25/12/2022 - 14:19
 *
 * @param url : url of image for thumbnail
 * @param imageResId : res id image, just for development
 * It just for a local test
 */
data class Thumbnail(
    val url: String?=null,
    val imageResId:Int?=null,
    val caption: String?=null
) : Serializable