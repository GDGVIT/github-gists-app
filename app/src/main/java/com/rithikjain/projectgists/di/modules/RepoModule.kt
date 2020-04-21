package com.rithikjain.projectgists.di.modules

import com.rithikjain.projectgists.repository.AppRepository
import org.koin.dsl.module

val repoModule = module {
    factory { AppRepository(get(), get()) }
}