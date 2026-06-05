package com.exemplo.copa2026.data.repository

import com.exemplo.copa2026.data.local.dao.PartidaMataMataDao
import com.exemplo.copa2026.data.model.PartidaMataMata
import kotlinx.coroutines.flow.Flow

class MataMataRepository(
    private val dao: PartidaMataMataDao
) {
    fun getPartidasByFase(fase: String): Flow<List<PartidaMataMata>> =
        dao.getPartidasByFase(fase)

    fun getAllPartidasMataMata(): Flow<List<PartidaMataMata>> =
        dao.getAllPartidasMataMata()

    fun getPartidaMataMataById(id: Int): Flow<PartidaMataMata?> =
        dao.getPartidaMataMataById(id)

    suspend fun atualizarTimesConfronto(partidaId: Int, timeCasaId: Int?, timeForaId: Int?) =
        dao.atualizarTimesConfronto(partidaId, timeCasaId, timeForaId)

    suspend fun salvarResultadoMataMata(partidaId: Int, golsCasa: Int, golsFora: Int) =
        dao.inserirResultadoMataMata(partidaId, golsCasa, golsFora)
}
