package com.exemplo.copa2026.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.exemplo.copa2026.data.model.Partida
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {

    @Query("SELECT * FROM partidas WHERE grupoId = :grupoId ORDER BY rodada")
    fun getPartidasByGrupo(grupoId: Int): Flow<List<Partida>>

    @Query("SELECT * FROM partidas WHERE id = :id")
    fun getPartidaById(id: Int): Flow<Partida?>

    @Update
    suspend fun updatePartida(partida: Partida)

    @Query("""
        UPDATE partidas
        SET golsCasa = :golsCasa, golsFora = :golsFora, realizado = 1
        WHERE id = :partidaId
    """)
    suspend fun inserirResultado(partidaId: Int, golsCasa: Int, golsFora: Int)

    @Query("SELECT COUNT(*) FROM partidas WHERE grupoId = :grupoId AND realizado = 1")
    suspend fun countPartidasRealizadasByGrupo(grupoId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartidas(partidas: List<Partida>)

    @Query("SELECT * FROM partidas ORDER BY id")
    fun getAllPartidas(): Flow<List<Partida>>

    @Query("SELECT COUNT(*) FROM partidas WHERE realizado = 1")
    suspend fun countRealizadas(): Int
}
