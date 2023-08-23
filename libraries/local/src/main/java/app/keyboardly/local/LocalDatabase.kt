package app.keyboardly.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.keyboardly.local.dao.AddOnDao
import app.keyboardly.local.dao.ClipboardDao
import app.keyboardly.local.model.AddOnModel
import app.keyboardly.local.model.ClipboardData
import app.keyboardly.local.util.Converters

const val DB_NAME = "db_keyboardly"
@Database(
    version = 16,
    entities = [ClipboardData::class, AddOnModel::class]
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun clipboardDao(): ClipboardDao
    abstract fun addOnDao(): AddOnDao
}
