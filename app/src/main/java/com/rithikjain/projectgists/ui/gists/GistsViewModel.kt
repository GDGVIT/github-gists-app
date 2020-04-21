package com.rithikjain.projectgists.ui.gists

import androidx.lifecycle.ViewModel
import com.rithikjain.projectgists.model.gists.CreateGistRequest
import com.rithikjain.projectgists.model.gists.DeleteGistRequest
import com.rithikjain.projectgists.model.gists.UpdateGistRequest
import com.rithikjain.projectgists.repository.AppRepository

class GistsViewModel(private val repo: AppRepository) : ViewModel() {

    var fileName = ""
    var fileCode = ""
    var fileDescription = ""

    fun viewAllGists() = repo.viewAllGists()

    fun createGist(createGistRequest: CreateGistRequest) = repo.createGist(createGistRequest)

    fun deleteGist(deleteGistRequest: DeleteGistRequest) = repo.deleteGist(deleteGistRequest)

    fun updateGist(updateGistRequest: UpdateGistRequest) = repo.updateGist(updateGistRequest)

}