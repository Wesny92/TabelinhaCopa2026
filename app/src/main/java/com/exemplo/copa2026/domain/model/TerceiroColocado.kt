package com.exemplo.copa2026.domain.model

import com.exemplo.copa2026.data.model.Time

data class TerceiroColocado(
    val time: Time,
    val grupoLetra: String,
    val classificacao: ClassificacaoTime,
    val classificado: Boolean = false
)
