package app.keyboardly.local.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Zainal on 25/12/2022 - 14:18
 */

class Converters {

    @TypeConverter
    fun fromThumbnail(value: List<app.keyboardly.local.model.Thumbnail>?): String? {
        return if (value.isNullOrEmpty()){
            null
        } else {
            val listType = object : TypeToken<List<app.keyboardly.local.model.Thumbnail>>() {}.type
            Gson().toJson(value, listType)
        }
    }

    @TypeConverter
    fun toThumbnail(stringValue: String?): List<app.keyboardly.local.model.Thumbnail>? {
        return if (stringValue.isNullOrEmpty()){
            null
        } else {
            val listType = object : TypeToken<List<app.keyboardly.local.model.Thumbnail>>() {}.type
            Gson().fromJson(stringValue, listType)
        }
    }
}