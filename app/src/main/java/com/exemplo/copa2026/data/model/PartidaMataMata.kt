package com.exemplo.copa2026.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partidas_mata_mata")
data class PartidaMataMata(
    @PrimaryKey
    val id: Int,
    val fase: String,
    val numeroJogo: Int,
    val timeCasaId: Int? = null,
    val timeForaId: Int? = null,
    val golsCasa: Int? = null,
    val golsFora: Int? = null,
    val realizado: Boolean = false,
    val partidaOrigemCasa: Int? = null,
    val partidaOrigemFora: Int? = null,
    val posicaoChave: Int,
    val data: String = "",
    val horario: String = "",
    val estadio: String = "",
    val cidade: String = "",
    val pais: String = "",
    val transmissao: String = ""
)
