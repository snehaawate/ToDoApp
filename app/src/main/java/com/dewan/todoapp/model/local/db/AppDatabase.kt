package com.dewan.todoapp.model.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dewan.todoapp.model.local.dao.TaskDao
import com.dewan.todoapp.model.local.db.migration.MIGRATION_2_3
import com.dewan.todoapp.model.local.entity.TaskEntity

@Database(entities = [TaskEntity::class],version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            synchronized(this){

                var instance = INSTANCE

                if (instance == null){
                    instance  = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "task_db"
                    )
                        //.fallbackToDestructiveMigration()
                        .addMigrations(MIGRATION_2_3)
                        .build()
                    INSTANCE = instance
                }
                return  instance
            }
        }
    }
}