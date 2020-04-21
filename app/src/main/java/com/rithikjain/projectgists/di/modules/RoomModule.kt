package com.rithikjain.projectgists.di.modules

import androidx.room.Room
import com.rithikjain.projectgists.db.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "gistappdb")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().appDao() }
}