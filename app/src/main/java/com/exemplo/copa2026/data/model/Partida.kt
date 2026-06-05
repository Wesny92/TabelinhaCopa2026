package com.exemplo.copa2026.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "partidas",
    foreignKeys = [
        ForeignKey(
            entity = Time::class,
            parentColumns = ["id"],
            childColumns = ["timeCasaId"]
        ),
        ForeignKey(
            entity = Time::class,
            parentColumns = ["id"],
            childColumns = ["timeForaId"]
        ),
        ForeignKey(
            entity = Grupo::class,
            parentColumns = ["id"],
            childColumns = ["grupoId"]
        )
    ],
    indices = [
        Index("timeCasaId"),
        Index("timeForaId"),
        Index("grupoId")
    ]
)
data class Partida(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timeCasaId: Int,
    val timeForaId: Int,
    val golsCasa: Int? = null,
    val golsFora: Int? = null,
    val rodada: Int,
    val grupoId: Int,
    val realizado: Boolean = false,
    val data: String = "",
    val horario: String = "",
    val estadio: String = "",
    val cidade: String = "",
    val pais: String = "",
    val transmissao: String = ""
)
