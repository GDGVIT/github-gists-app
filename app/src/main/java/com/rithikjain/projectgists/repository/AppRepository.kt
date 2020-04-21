package com.rithikjain.projectgists.repository

import com.rithikjain.projectgists.db.AppDao
import com.rithikjain.projectgists.model.gists.CreateGistRequest
import com.rithikjain.projectgists.model.gists.DeleteGistRequest
import com.rithikjain.projectgists.model.gists.UpdateGistRequest
import com.rithikjain.projectgists.model.register.RegisterRequest
import com.rithikjain.projectgists.network.ApiClient

class AppRepository(private val apiClient: ApiClient, private val appDao: AppDao) : BaseRepo() {

    fun registerUser(registerRequest: RegisterRequest) =
        makeRequest { apiClient.registerUser(registerRequest) }

    fun viewAllGists() = makeRequestAndSave(
        databaseQuery = { appDao.getAllGists() },
        networkCall = { apiClient.viewAllGists() },
        saveCallResult = { appDao.insertGists(it.Files) }
    )

    fun createGist(createGistRequest: CreateGistRequest) =
        makeRequest { apiClient.createGist(createGistRequest) }

    fun deleteGist(deleteGistRequest: DeleteGistRequest) = deleteFromNetworkAndDB(
        databaseQuery = { appDao.deleteGist(deleteGistRequest.Filename, deleteGistRequest.GistID) },
        networkCall = { apiClient.deleteGist(deleteGistRequest) }
    )

    fun updateGist(updateGistRequest: UpdateGistRequest) =
        makeRequest { apiClient.updateGist(updateGistRequest) }

    suspend fun deleteLocalGist() = appDao.deleteAllGists()

}