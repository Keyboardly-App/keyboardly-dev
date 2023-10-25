package app.keyboardly.addon.sample.data.local

import android.content.Context
import androidx.room.Room

/**
 * Created by zainal on 24/10/23 - 02:10 PM
 */
class SampleDbManager private constructor(
    private val database: SampleDatabase
) {
    fun provinceDao() = database.provinceDao()

    companion object {
        @Volatile
        private var INSTANCE: SampleDbManager? = null

        @JvmStatic
        fun getInstance(context: Context): SampleDbManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: run {
                    val db = Room.databaseBuilder(
                        context,
                        SampleDatabase::class.java, DB_NAME
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                    SampleDbManager(db).also { INSTANCE = it }
                }
            }
        }
    }
}