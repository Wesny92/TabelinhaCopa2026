package com.exemplo.copa2026.ui.grupo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.exemplo.copa2026.domain.model.ClassificacaoTime
import com.exemplo.copa2026.ui.theme.FifaGold

@Composable
fun TabelaGrupo(
    tabela: List<ClassificacaoTime>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Text("#", Modifier.weight(0.3f), fontWeight = FontWeight.Bold)
        Text("Time", Modifier.weight(1.4f), fontWeight = FontWeight.Bold)
        Text("J", Modifier.weight(0.3f), fontWeight = FontWeight.Bold)
        Text("V", Modifier.weight(0.3f), fontWeight = FontWeight.Bold)
        Text("D", Modifier.weight(0.3f), fontWeight = FontWeight.Bold)
        Text("GP", Modifier.weight(0.4f), fontWeight = FontWeight.Bold)
        Text("GC", Modifier.weight(0.4f), fontWeight = FontWeight.Bold)
        Text("SG", Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
        Text("P", Modifier.weight(0.4f), fontWeight = FontWeight.Bold, color = FifaGold)
    }

    tabela.forEachIndexed { index, item ->
        val bgColor = when (index) {
            0 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            1 -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.10f)
            else -> MaterialTheme.colorScheme.surface
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor)
                .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
            Text("${index + 1}", Modifier.weight(0.3f))
            Text(item.time.nome, Modifier.weight(1.4f))
            Text("${item.jogos}", Modifier.weight(0.3f))
            Text("${item.vitorias}", Modifier.weight(0.3f))
            Text("${item.derrotas}", Modifier.weight(0.3f))
            Text("${item.golsPro}", Modifier.weight(0.4f))
            Text("${item.golsContra}", Modifier.weight(0.4f))
            Text("${item.saldoGols}", Modifier.weight(0.5f))
            Text("${item.pontos}", Modifier.weight(0.4f), color = FifaGold, fontWeight = FontWeight.Bold)
        }
    }
}
