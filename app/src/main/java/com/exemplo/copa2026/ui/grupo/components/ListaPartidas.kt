package com.exemplo.copa2026.ui.grupo.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.exemplo.copa2026.ui.theme.FWC2026Font
import com.exemplo.copa2026.ui.theme.GreenLight

@Composable
fun ListaPartidas(
    partidas: List<PartidaItem>,
    onPartidaClick: (Int) -> Unit
) {
    val porRodada = partidas.groupBy { it.rodada }

    porRodada.forEach { (rodada, lista) ->
        Text(
            text = "Rodada $rodada",
            fontFamily = FWC2026Font,
            fontWeight = FontWeight.Black,
            color = GreenLight,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            textAlign = TextAlign.Center
        )
        lista.forEach { item ->
            ItemPartida(
                timeCasaEmoji = item.timeCasaEmoji,
                timeCasaNome = item.timeCasaNome,
                timeForaNome = item.timeForaNome,
                timeForaEmoji = item.timeForaEmoji,
                golsCasa = item.golsCasa,
                golsFora = item.golsFora,
                realizado = item.realizado,
                onClick = { onPartidaClick(item.partidaId) }
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}

data class PartidaItem(
    val partidaId: Int,
    val rodada: Int,
    val timeCasaEmoji: String,
    val timeCasaNome: String,
    val timeForaNome: String,
    val timeForaEmoji: String,
    val golsCasa: Int?,
    val golsFora: Int?,
    val realizado: Boolean,
    val data: String = "",
    val horario: String = "",
    val estadio: String = "",
    val cidade: String = "",
    val pais: String = ""
)
