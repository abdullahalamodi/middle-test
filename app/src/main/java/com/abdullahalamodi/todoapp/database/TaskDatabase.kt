package com.abdullahalamodi.todoapp.database
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abdullahalamodi.todoapp.Task

@Database(entities = [ Task::class ], version=1,exportSchema = false)
@TypeConverters(TasksTypeConverters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao():TasksDao;
}

//val migration_1_2 = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL(
//            "ALTER TABLE Task ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
//        )
//    }
//}
//