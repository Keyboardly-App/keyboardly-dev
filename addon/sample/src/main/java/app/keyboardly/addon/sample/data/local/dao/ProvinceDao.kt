package app.keyboardly.addon.sample.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import app.keyboardly.addon.sample.data.model.Province
import app.keyboardly.addon.sample.data.model.TABLE_PROVINCE

@Dao
abstract class ProvinceDao :
    BaseDao<Province> {

    @androidx.room.Transaction
    @Query("SELECT * from $TABLE_PROVINCE")
    abstract fun getProvinceAsync(): LiveData<List<Province>?>

    @androidx.room.Transaction
    @Query("SELECT * from $TABLE_PROVINCE")
    abstract fun getProvinces(): List<Province>?

    @androidx.room.Transaction
    @Query("DELETE FROM $TABLE_PROVINCE")
    abstract fun deleteAll()
}