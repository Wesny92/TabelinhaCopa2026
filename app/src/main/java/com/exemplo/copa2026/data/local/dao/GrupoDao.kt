package com.exemplo.copa2026.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.exemplo.copa2026.data.model.Grupo
import com.exemplo.copa2026.data.model.GrupoComTimes
import com.exemplo.copa2026.data.model.GrupoComTudo
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoDao {

    @Query("SELECT * FROM grupos ORDER BY id")
    fun getAllGrupos(): Flow<List<Grupo>>

    @Query("SELECT * FROM grupos WHERE id = :id")
    fun getGrupoById(id: Int): Flow<Grupo>

    @Transaction
    @Query("SELECT * FROM grupos WHERE id = :grupoId")
    fun getGrupoComTimes(grupoId: Int): Flow<GrupoComTimes>

    @Transaction
    @Query("SELECT * FROM grupos WHERE id = :grupoId")
    fun getGrupoComTudo(grupoId: Int): Flow<GrupoComTudo>

    @Transaction
    @Query("SELECT * FROM grupos ORDER BY id")
    fun getAllGruposComTudo(): Flow<List<GrupoComTudo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrupos(grupos: List<Grupo>)
}
