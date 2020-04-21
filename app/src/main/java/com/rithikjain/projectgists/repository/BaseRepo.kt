package com.rithikjain.projectgists.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.rithikjain.projectgists.model.Result
import kotlinx.coroutines.Dispatchers

open class BaseRepo {

    protected fun <T> makeRequest(request: suspend () -> Result<T>) = liveData {
        emit(Result.loading())

        val response = request()

        when (response.status) {
            Result.Status.SUCCESS -> {
                emit(Result.success(response.data))
            }
            Result.Status.ERROR -> {
                emit(Result.error(response.message!!))
            }
            else -> {
            }
        }
    }

    protected fun <T> deleteFromNetworkAndDB(
        databaseQuery: suspend () -> Unit,
        networkCall: suspend () -> Result<T>
    ) = liveData(Dispatchers.IO) {
        emit(Result.loading())

        val response = networkCall()

        when (response.status) {
            Result.Status.SUCCESS -> {
                emit(Result.success(response.data))
                databaseQuery.invoke()
            }
            Result.Status.ERROR -> {
                if (response.message!! == "404 Not Found") {
                    databaseQuery.invoke()
                    emit(Result.error(response.message))
                } else {
                    emit(Result.error(response.message))
                }
            }
            else -> {
            }
        }
    }

    protected fun <T, A> makeRequestAndSave(
        databaseQuery: () -> LiveData<T>,
        networkCall: suspend () -> Result<A>,
        saveCallResult: suspend (A) -> Unit
    ): LiveData<Result<T>> = liveData(Dispatchers.IO) {
        emit(Result.loading())

        val source = databaseQuery.invoke().map { Result.success(it) }
        emitSource(source)

        val response = networkCall.invoke()
        when (response.status) {
            Result.Status.SUCCESS -> {
                saveCallResult(response.data!!)
            }
            Result.Status.ERROR -> {
                emit(Result.error(response.message!!))
                emitSource(source)
            }
            else -> {
            }
        }
    }
}