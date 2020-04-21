package com.rithikjain.projectgists.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rithikjain.projectgists.repository.AppRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: AppRepository): ViewModel() {

    fun deleteLocalGist() = viewModelScope.launch {
        repo.deleteLocalGist()
    }

}