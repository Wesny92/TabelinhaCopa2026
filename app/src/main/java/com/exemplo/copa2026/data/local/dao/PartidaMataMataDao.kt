package com.exemplo.copa2026.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.exemplo.copa2026.data.model.PartidaMataMata
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaMataMataDao {

    @Query("SELECT * FROM partidas_mata_mata WHERE fase = :fase ORDER BY numeroJogo")
    fun getPartidasByFase(fase: String): Flow<List<PartidaMataMata>>

    @Query("SELECT * FROM partidas_mata_mata ORDER BY fase, numeroJogo")
    fun getAllPartidasMataMata(): Flow<List<PartidaMataMata>>

    @Query("SELECT * FROM partidas_mata_mata WHERE id = :id")
    fun getPartidaMataMataById(id: Int): Flow<PartidaMataMata?>

    @Update
    suspend fun updatePartidaMataMata(partida: PartidaMataMata)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartidasMataMata(partidas: List<PartidaMataMata>)

    @Query("""
        UPDATE partidas_mata_mata
        SET timeCasaId = :timeCasaId, timeForaId = :timeForaId
        WHERE id = :partidaId
    """)
    suspend fun atualizarTimesConfronto(partidaId: Int, timeCasaId: Int?, timeForaId: Int?)

    @Query("""
        UPDATE partidas_mata_mata
        SET golsCasa = :golsCasa, golsFora = :golsFora, realizado = 1
        WHERE id = :partidaId
    """)
    suspend fun inserirResultadoMataMata(partidaId: Int, golsCasa: Int, golsFora: Int)
}
