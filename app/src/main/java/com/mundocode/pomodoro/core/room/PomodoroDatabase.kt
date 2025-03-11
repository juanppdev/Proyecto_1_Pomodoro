package com.mundocode.pomodoro.core.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mundocode.pomodoro.core.room.converters.DateConverter
import com.mundocode.pomodoro.core.room.converters.StringListConverter
import com.mundocode.pomodoro.core.room.dao.HabitsDao
import com.mundocode.pomodoro.core.room.dao.PurchasedItemsDao
import com.mundocode.pomodoro.core.room.dao.PurchasedThemeDao
import com.mundocode.pomodoro.core.room.dao.SessionDao
import com.mundocode.pomodoro.core.room.dao.TaskDao
import com.mundocode.pomodoro.core.room.dao.UserPointsDao
import com.mundocode.pomodoro.model.room.HabitsEntity
import com.mundocode.pomodoro.model.room.PurchasedItemEntity
import com.mundocode.pomodoro.model.room.PurchasedThemeEntity
import com.mundocode.pomodoro.model.room.SessionEntity
import com.mundocode.pomodoro.model.room.TaskEntity
import com.mundocode.pomodoro.model.room.UserPointsEntity

@Database(
    entities = [
        TaskEntity::class,
        HabitsEntity::class,
        SessionEntity::class,
        UserPointsEntity::class,
        PurchasedItemEntity::class,
        PurchasedThemeEntity::class,
    ],

    version = 1,
)
@TypeConverters(
    value = [
        StringListConverter::class,
        DateConverter::class,
    ],
)
abstract class PomodoroDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun habitsDao(): HabitsDao
    abstract fun sessionDao(): SessionDao
    abstract fun userPointsDao(): UserPointsDao
    abstract fun purchasedItemsDao(): PurchasedItemsDao
    abstract fun purchasedThemeDao(): PurchasedThemeDao

    companion object {

        private const val APP_DATABASE_NAME = "pomodoro_database"
        const val TASK_TABLE_NAME = "task"
        const val HABITS_TABLE_NAME = "habits"
        const val SESSION_TABLE_NAME = "session"
        const val USER_POINTS_TABLE_NAME = "user_points"
        const val PURCHASED_ITEMS_TABLE_NAME = "purchased_items"
        const val PURCHASED_THEME_TABLE_NAME = "purchased_theme"

        // For Singleton instantiation
        @Volatile
        private var instance: PomodoroDatabase? = null

        fun getInstance(context: Context): PomodoroDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): PomodoroDatabase =
            Room.databaseBuilder(context, PomodoroDatabase::class.java, APP_DATABASE_NAME)
                .addMigrations(
                    *arrayOf(
//                        MIGRATION_1_2,
//                        MIGRATION_2_3,
                    ),
                )
                .build()

        // /////////////////////////////////////////////////////////////////////////
        // Migrations
        // /////////////////////////////////////////////////////////////////////////
//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE $GROUPS_TABLE_NAME ADD COLUMN lastUpdated INTEGER NULL")
//            }
//        }
//        val MIGRATION_2_3 = object : Migration(2, 3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // 1. Create the new table without 'groupIds'
//                database.execSQL(
//                    """
//            CREATE TABLE users_table_new (
//                userId TEXT PRIMARY KEY NOT NULL,
//                email TEXT NOT NULL,
//                language TEXT NOT NULL,
//                watched TEXT NOT NULL DEFAULT '[]',
//                toWatch TEXT NOT NULL DEFAULT '[]'
//            )
//                    """.trimIndent(),
//                )
//
//                // 2. Copy old data table in the new one excluding 'groupIds'
//                database.execSQL(
//                    """
//            INSERT INTO users_table_new (userId, email, language, watched, toWatch)
//            SELECT userId, email, language, '[]', '[]' FROM users_table
//                    """.trimIndent(),
//                )
//
//                // 3. Delete the old table
//                database.execSQL("DROP TABLE users_table")
//
//                // 4. Rename the new table with the original one
//                database.execSQL("ALTER TABLE users_table_new RENAME TO users_table")
//            }
//        }
    }
}
