package com.np.testroom.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.np.testroom.daos.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.np.testroom.converters.Converters
import androidx.room.TypeConverters

@Database(entities = [User::class, Product::class, Scenario::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun scenarioDao(): ScenarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4,MIGRATION_4_5,MIGRATION_5_6) // Add the migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Example: Create the new 'Product' table
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS `products` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `name` TEXT NOT NULL, 
                `price` REAL NOT NULL)
        """)
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Alter the "users" table to add new columns
                database.execSQL("ALTER TABLE users ADD COLUMN username TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE users ADD COLUMN password_hash TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE users ADD COLUMN description TEXT")
                database.execSQL("ALTER TABLE users ADD COLUMN profession TEXT")
                database.execSQL("ALTER TABLE users ADD COLUMN created_at INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE users ADD COLUMN updated_at INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the 'scenarios' table
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `scenarios` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `title` TEXT NOT NULL, 
                        `loan` REAL NOT NULL,
                        `interest_rate` REAL NOT NULL,
                        `period` INTEGER NOT NULL, 
                        `term` TEXT NOT NULL, 
                        `extra_monthly_payment` REAL, 
                        `user_id` INTEGER NOT NULL, 
                        `category` TEXT,
                        FOREIGN KEY(`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
                    )
                """)
                database.execSQL("ALTER TABLE scenarios ADD COLUMN start_date INTEGER DEFAULT ${System.currentTimeMillis()}") // Add start_date
                database.execSQL("ALTER TABLE scenarios ADD COLUMN created_at INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE scenarios ADD COLUMN updated_at INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {


            }
        }
        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users ADD COLUMN email TEXT NOT NULL DEFAULT 'fernandosanchezmontevideo@gmail.com'")
            }
        }
    }
}
