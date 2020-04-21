package com.rithikjain.projectgists.di

import com.rithikjain.projectgists.di.modules.apiModule
import com.rithikjain.projectgists.di.modules.repoModule
import com.rithikjain.projectgists.di.modules.roomModule
import com.rithikjain.projectgists.di.modules.viewModelModule

val appComponent = listOf(roomModule, apiModule, repoModule, viewModelModule)