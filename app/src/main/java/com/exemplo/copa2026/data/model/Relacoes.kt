package com.exemplo.copa2026.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class GrupoComTimes(
    @Embedded val grupo: Grupo,
    @Relation(
        parentColumn = "id",
        entityColumn = "grupoId"
    )
    val times: List<Time>
)

data class GrupoComTudo(
    @Embedded val grupo: Grupo,
    @Relation(
        parentColumn = "id",
        entityColumn = "grupoId"
    )
    val times: List<Time>,
    @Relation(
        parentColumn = "id",
        entityColumn = "grupoId"
    )
    val partidas: List<Partida>
)
