package com.rithikjain.projectgists.network

import com.rithikjain.projectgists.model.gists.CreateGistRequest
import com.rithikjain.projectgists.model.gists.DeleteGistRequest
import com.rithikjain.projectgists.model.gists.UpdateGistRequest
import com.rithikjain.projectgists.model.register.RegisterRequest

class ApiClient(private val api: ApiInterface) : BaseApiClient() {

    suspend fun registerUser(registerRequest: RegisterRequest) =
        getResult { api.registerUser(registerRequest) }

    suspend fun viewAllGists() = getResult { api.viewAllGists() }

    suspend fun createGist(createGistRequest: CreateGistRequest) =
        getResult { api.createGist(createGistRequest) }

    suspend fun deleteGist(deleteGistRequest: DeleteGistRequest) =
        getResult { api.deleteGist(deleteGistRequest) }

    suspend fun updateGist(updateGistRequest: UpdateGistRequest) =
        getResult { api.updateGist(updateGistRequest) }

}