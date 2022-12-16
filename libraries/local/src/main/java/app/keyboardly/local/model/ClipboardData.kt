package app.keyboardly.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_clipboard")
class ClipboardData(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val text:String?,
    val path:String?=null,
)