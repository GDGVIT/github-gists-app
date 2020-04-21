package com.rithikjain.projectgists

import android.app.Application
import com.rithikjain.projectgists.di.appComponent
import io.github.kbiakov.codeview.classifier.CodeProcessor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        CodeProcessor.init(this)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appComponent)
        }
    }
}