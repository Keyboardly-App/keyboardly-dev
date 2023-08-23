package app.keyboardly.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import app.keyboardly.local.model.AddOnModel

@Dao
abstract class AddOnDao : BaseDao<AddOnModel> {

    @androidx.room.Transaction
    @Query("SELECT * from table_addon ORDER BY id DESC")
    abstract fun getAddOnAsync(): LiveData<List<AddOnModel>?>

    @androidx.room.Transaction
    @Query("SELECT * from table_addon where installed = 1")
    abstract fun getInstalledAddOn(): MutableList<AddOnModel>?

    @androidx.room.Transaction
    @Query("SELECT * from table_addon where installed = 1 AND id=:id")
    abstract fun getInstalledById(id: Int): AddOnModel?

    @androidx.room.Transaction
    @Query("SELECT * from table_addon where id=:id")
    abstract fun getAddOnById(id: Int): AddOnModel?

    @androidx.room.Transaction
    @Query("SELECT * from table_addon")
    abstract fun getAddOn(): MutableList<AddOnModel>?

    @androidx.room.Transaction
    @Query("DELETE FROM table_addon")
    abstract fun deleteAll()
}