package com.exemplo.copa2026.data.repository

import com.exemplo.copa2026.data.local.dao.GrupoDao
import com.exemplo.copa2026.data.local.dao.PartidaDao
import com.exemplo.copa2026.data.local.dao.TimeDao
import com.exemplo.copa2026.data.model.Grupo
import com.exemplo.copa2026.data.model.GrupoComTudo
import com.exemplo.copa2026.data.model.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GrupoRepository(
    private val grupoDao: GrupoDao,
    private val timeDao: TimeDao,
    private val partidaDao: PartidaDao
) {
    fun getAllGrupos(): Flow<List<Grupo>> = grupoDao.getAllGrupos()

    fun getGrupoComTudo(grupoId: Int): Flow<GrupoComTudo> =
        grupoDao.getGrupoComTudo(grupoId)

    fun getTimesByGrupo(grupoId: Int): Flow<List<Time>> =
        timeDao.getTimesByGrupo(grupoId)

    fun getAllGruposComTudo(): Flow<List<GrupoComTudo>> =
        grupoDao.getAllGruposComTudo()

    suspend fun updateTime(time: Time) = timeDao.updateTime(time)
}
