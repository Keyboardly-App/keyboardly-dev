package app.keyboardly.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


/**
 * This for database keyboard local
 */
class DatabaseManager private constructor(
    private val database: LocalDatabase
) {

    fun clipboardDao() = database.clipboardDao()
    fun addOnDao() = database.addOnDao()
    fun clearAllTables() = database.clearAllTables()

    companion object {
        @Volatile
        var INSTANCE: DatabaseManager? = null

        private val MIGRATION_10_11: Migration = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL("CREATE TABLE IF NOT EXISTS table_addon (" +
                        "id INTEGER NOT NULL," +
                        "featureNameId TEXT NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "description TEXT NOT NULL, " +
                        "developer TEXT NOT NULL, linkDetail TEXT NOT NULL," +
                        "iconUrl TEXT NOT NULL, " +
                        "price INTEGER DEFAULT 0, " +
                        "featurePackageId TEXT DEFAULT NULL, " +
                        "developerLink TEXT DEFAULT NULL, " +
                        "downloadCount INTEGER DEFAULT 0, " +
                        "helpLink TEXT DEFAULT NULL, authorLink TEXT DEFAULT NULL, " +
                        "termLink TEXT DEFAULT NULL, privacyLink TEXT DEFAULT NULL, " +
                        "installed INTEGER DEFAULT 0, PRIMARY KEY(id))")
            }
        }

        @JvmStatic
        fun getInstance(context: Context): DatabaseManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: run {
                    val db = Room.databaseBuilder(
                        context, LocalDatabase::class.java, DB_NAME
                    )
                        .allowMainThreadQueries()
//                        .addMigrations(MIGRATION_10_11)
                        .fallbackToDestructiveMigration()
                        .build()
                    DatabaseManager(db).also { INSTANCE = it }
                }
            }
        }
    }
}