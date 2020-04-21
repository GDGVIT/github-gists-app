package com.rithikjain.projectgists.di.modules

import com.rithikjain.projectgists.network.ApiClient
import com.rithikjain.projectgists.network.ApiService
import org.koin.dsl.module

val apiModule = module {
    factory { ApiService.createRetrofit(get()) }
    factory { ApiClient(get()) }
}