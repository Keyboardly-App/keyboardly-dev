package app.keyboardly.addon.sample.data.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Zainal on 20/10/2023 - 13:55
 */

const val TABLE_PROVINCE = "tb_province"
@Entity(tableName = TABLE_PROVINCE)
@Keep
data class Province(
    @PrimaryKey
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    var date_time: String?=null,
)