package com.rithikjain.projectgists.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rithikjain.projectgists.model.gists.File

@Database(entities = [File::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}