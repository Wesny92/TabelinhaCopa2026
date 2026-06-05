package com.exemplo.copa2026.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grupos")
data class Grupo(
    @PrimaryKey
    val id: Int,
    val letra: String,
    val nome: String
)
