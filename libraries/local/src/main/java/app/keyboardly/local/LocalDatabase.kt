package app.keyboardly.local

import androidx.room.Database
import androidx.room.RoomDatabase
import app.keyboardly.local.dao.AddOnDao
import app.keyboardly.local.dao.ClipboardDao
import app.keyboardly.local.model.AddOnModel
import app.keyboardly.local.model.ClipboardData

const val DB_NAME = "db_woowa_keyboard"
@Database(
    version = 11,
    entities = [ClipboardData::class, AddOnModel::class]
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun clipboardDao(): ClipboardDao
    abstract fun addOnDao(): AddOnDao
}
