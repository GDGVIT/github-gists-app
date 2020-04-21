package com.rithikjain.projectgists.network

import com.rithikjain.projectgists.model.gists.CreateGistRequest
import com.rithikjain.projectgists.model.gists.DeleteGistRequest
import com.rithikjain.projectgists.model.gists.UpdateGistRequest
import com.rithikjain.projectgists.model.gists.ViewGistsResponse
import com.rithikjain.projectgists.model.register.RegisterRequest
import com.rithikjain.projectgists.model.register.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiInterface {

    @POST("user/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("gists/view")
    suspend fun viewAllGists(): Response<ViewGistsResponse>

    @POST("gists/create")
    suspend fun createGist(@Body createGistRequest: CreateGistRequest): Response<ViewGistsResponse>

    @POST("gists/delete")
    suspend fun deleteGist(@Body deleteGistRequest: DeleteGistRequest): Response<DeleteGistRequest>

    @POST("gists/update")
    suspend fun updateGist(@Body updateGistRequest: UpdateGistRequest): Response<ViewGistsResponse>

}