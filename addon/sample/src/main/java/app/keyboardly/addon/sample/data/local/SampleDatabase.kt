package app.keyboardly.addon.sample.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import app.keyboardly.addon.sample.data.local.dao.ProvinceDao
import app.keyboardly.addon.sample.data.model.Province

/**
 * Created by Zainal on 24/10/2023 - 02:32 PM
 */

// start with feature name / add on id
const val DB_NAME = "sample_db"

@Database(
    version = 2,
    entities = [Province::class]
)

abstract class SampleDatabase : RoomDatabase() {
    abstract fun provinceDao(): ProvinceDao
}
