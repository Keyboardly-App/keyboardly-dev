package app.keyboardly.local.model

import androidx.annotation.Keep
import java.io.Serializable

/**
 * Created by Zainal on 25/12/2022 - 14:19
 *
 * @param url : url of image for thumbnail
 * @param imageResId : res id image, just for development
 * It just for a local test
 */
@Keep
data class Thumbnail(
    val url: String?=null,
    val imageResId:Int?=null,
    val caption: String?=null
) : Serializable