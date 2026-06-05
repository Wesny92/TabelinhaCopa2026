package com.exemplo.copa2026.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.exemplo.copa2026.data.model.Time
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeDao {

    @Query("SELECT * FROM times WHERE grupoId = :grupoId ORDER BY nome")
    fun getTimesByGrupo(grupoId: Int): Flow<List<Time>>

    @Query("SELECT * FROM times WHERE id = :id")
    fun getTimeById(id: Int): Flow<Time?>

    @Query("SELECT * FROM times")
    fun getAllTimes(): Flow<List<Time>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimes(times: List<Time>)

    @Update
    suspend fun updateTime(time: Time)
}
