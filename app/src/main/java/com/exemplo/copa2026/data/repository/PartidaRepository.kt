package com.exemplo.copa2026.data.repository

import com.exemplo.copa2026.data.local.dao.PartidaDao
import com.exemplo.copa2026.data.model.Partida
import kotlinx.coroutines.flow.Flow

class PartidaRepository(
    private val partidaDao: PartidaDao
) {
    fun getPartidasByGrupo(grupoId: Int): Flow<List<Partida>> =
        partidaDao.getPartidasByGrupo(grupoId)

    suspend fun salvarResultado(partidaId: Int, golsCasa: Int, golsFora: Int) =
        partidaDao.inserirResultado(partidaId, golsCasa, golsFora)

    suspend fun countPartidasRealizadasByGrupo(grupoId: Int): Int =
        partidaDao.countPartidasRealizadasByGrupo(grupoId)

    suspend fun countRealizadas(): Int = partidaDao.countRealizadas()
}
