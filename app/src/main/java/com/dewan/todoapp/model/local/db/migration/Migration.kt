package com.dewan.todoapp.model.local.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2,3){

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE task_entity ADD COLUMN note TEXT NOT NULL DEFAULT ''")
    }

}