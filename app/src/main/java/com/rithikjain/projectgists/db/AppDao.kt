package com.rithikjain.projectgists.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rithikjain.projectgists.model.gists.File

@Dao
interface AppDao {

    @Query("SELECT * FROM gists")
    fun getAllGists(): LiveData<List<File>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGists(gists: List<File>)

    @Query("DELETE FROM gists")
    suspend fun deleteAllGists()

    @Query("DELETE FROM gists WHERE Filename=:filename AND GistID=:gistID")
    suspend fun deleteGist(filename: String, gistID: String)

}