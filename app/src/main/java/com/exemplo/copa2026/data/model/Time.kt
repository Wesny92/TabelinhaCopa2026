package com.exemplo.copa2026.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "times",
    foreignKeys = [
        ForeignKey(
            entity = Grupo::class,
            parentColumns = ["id"],
            childColumns = ["grupoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("grupoId")]
)
data class Time(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val sigla: String,
    val bandeiraEmoji: String,
    val grupoId: Int
)
