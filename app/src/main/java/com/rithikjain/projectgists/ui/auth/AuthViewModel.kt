package com.rithikjain.projectgists.ui.auth

import androidx.lifecycle.ViewModel
import com.rithikjain.projectgists.model.register.RegisterRequest
import com.rithikjain.projectgists.repository.AppRepository

class AuthViewModel(private val repo: AppRepository) : ViewModel() {

    fun registerUser(registerRequest: RegisterRequest) = repo.registerUser(registerRequest)

}