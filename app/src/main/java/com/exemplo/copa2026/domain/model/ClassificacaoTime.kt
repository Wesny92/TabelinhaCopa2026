package com.exemplo.copa2026.domain.model

import com.exemplo.copa2026.data.model.Time

data class ClassificacaoTime(
    val time: Time,
    val pontos: Int = 0,
    val jogos: Int = 0,
    val vitorias: Int = 0,
    val empates: Int = 0,
    val derrotas: Int = 0,
    val golsPro: Int = 0,
    val golsContra: Int = 0,
    val saldoGols: Int = 0
)
