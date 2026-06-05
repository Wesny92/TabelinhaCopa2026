package com.exemplo.copa2026.domain.model

import com.exemplo.copa2026.data.model.PartidaMataMata
import com.exemplo.copa2026.data.model.Time

data class ConfrontoChave(
    val partida: PartidaMataMata,
    val timeCasa: Time? = null,
    val timeFora: Time? = null,
    val grupoCasa: String? = null,
    val grupoFora: String? = null,
    val golsCasa: Int? = null,
    val golsFora: Int? = null,
    val vencedor: Time? = null
)
