package app.keyboardly.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import app.keyboardly.local.model.ClipboardData

@Dao
abstract class ClipboardDao : BaseDao<ClipboardData> {

    @androidx.room.Transaction
    @Query("SELECT DISTINCT text, id from table_clipboard ORDER BY id DESC")
    abstract fun getClipboardsAsync(): LiveData<List<ClipboardData>?>

    @androidx.room.Transaction
    @Query("SELECT * from table_clipboard ORDER BY id DESC")
    abstract fun getClipboards(): MutableList<ClipboardData>?
    @androidx.room.Transaction
    @Query("SELECT * from table_clipboard WHERE path IS NULL ORDER BY id DESC")
    abstract fun getClipboardsText(): MutableList<ClipboardData>?

    @androidx.room.Transaction
    @Query("SELECT * from table_clipboard where text=:string LIMIT 1")
    abstract fun isSaved(string: String): ClipboardData?

    @androidx.room.Transaction
    @Query("DELETE FROM table_clipboard")
    abstract fun deleteAll()
}