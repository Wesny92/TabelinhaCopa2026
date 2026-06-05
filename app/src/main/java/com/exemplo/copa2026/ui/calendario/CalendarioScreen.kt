package com.exemplo.copa2026.ui.calendario

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exemplo.copa2026.di.AppContainer
import com.exemplo.copa2026.R
import com.exemplo.copa2026.ui.theme.FifaDark
import com.exemplo.copa2026.ui.theme.FifaGold
import com.exemplo.copa2026.ui.theme.FWC2026Font
import com.exemplo.copa2026.ui.theme.GreenLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarioScreen(
    container: AppContainer,
    onBack: () -> Unit,
    viewModel: CalendarioViewModel = viewModel(
        factory = CalendarioViewModelFactory(container)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth().height(64.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    drawRect(FifaDark)
                    val goldLine = FifaGold.copy(alpha = 0.12f)
                    val step = 55f
                    var x = -h
                    while (x < w + h) {
                        drawLine(goldLine, Offset(x, h), Offset(x + h, 0f), strokeWidth = 1.5f)
                        x += step
                    }
                    val blueLine = Color(0xFF42A5F5).copy(alpha = 0.10f)
                    val diamondW = 70f
                    val diamondH = 30f
                    for (col in 0..((w / diamondW).toInt() + 1)) {
                        for (row in 0..((h / diamondH).toInt() + 1)) {
                            val cx = (col * diamondW) + (if (row % 2 == 0) 0f else diamondW / 2f)
                            val cy = row * diamondH
                            val path = Path().apply {
                                moveTo(cx, cy - diamondH / 2f)
                                lineTo(cx + diamondW / 3f, cy)
                                lineTo(cx, cy + diamondH / 2f)
                                lineTo(cx - diamondW / 3f, cy)
                                close()
                            }
                            drawPath(path, blueLine, style = Stroke(1f))
                        }
                    }
                    drawLine(FifaGold.copy(alpha = 0.3f), Offset(0f, h - 2f), Offset(w, h - 2f), strokeWidth = 2f)
                }
                Box(
                    modifier = Modifier.fillMaxSize().padding(top = 28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Calendário",
                        fontFamily = FWC2026Font,
                        fontWeight = FontWeight.Black,
                        color = FifaGold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                }
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart).padding(top = 28.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = FifaGold)
                }
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            androidx.compose.foundation.layout.Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val porData = uiState.jogos.groupBy { it.data }
            val ordemMeses = mapOf("Jan" to 1, "Fev" to 2, "Mar" to 3, "Abr" to 4,
                                   "Mai" to 5, "Jun" to 6, "Jul" to 7, "Ago" to 8,
                                   "Set" to 9, "Out" to 10, "Nov" to 11, "Dez" to 12)
            val datasOrdenadas = porData.keys.sortedBy { data ->
                val partes = data.split(" ")
                val dia = partes[0].toIntOrNull() ?: 0
                val mes = ordemMeses[partes.getOrElse(1) { "" }] ?: 0
                mes * 100 + dia
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 8.dp)
            ) {
                datasOrdenadas.forEach { data ->
                    item {
                        Text(
                            text = data,
                            fontFamily = FWC2026Font,
                            fontWeight = FontWeight.Black,
                            color = GreenLight,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
                        )
                    }
                    val jogosDoDia = porData[data] ?: emptyList()
                    items(jogosDoDia) { jogo ->
                        CardJogoCalendario(jogo)
                    }
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
}
        }
    }
}
    }
}

@Composable
private fun CardJogoCalendario(jogo: CalendarioViewModel.JogoCalendario) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (jogo.fase.isEmpty()) "Grupo ${jogo.grupoLetra}" else jogo.grupoLetra,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    jogo.horario,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "${jogo.cidade}, ${jogo.pais} - ${jogo.estadio}",
                    fontSize = 12.sp,
                    maxLines = 1
                )
                Text(
                    "${jogo.emojiCasa} ${jogo.nomeCasa}  x  ${jogo.nomeFora} ${jogo.emojiFora}",
                    fontSize = 13.sp,
                    maxLines = 1
                )
            }
        }

        if (jogo.transmissao.isNotEmpty()) {
            val logos = mapOf(
                "CazeTV" to R.drawable.logo_cazetv,
                "Globo" to R.drawable.logo_globo
            )
            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Transmissão", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(8.dp))
                val ids = jogo.transmissao.split(",").map { it.trim() }.filter { it in logos }.map { logos[it]!! }
                ids.forEachIndexed { i, resId ->
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        modifier = Modifier.height(20.dp).clip(RoundedCornerShape(4.dp))
                    )
                    if (i < ids.lastIndex) Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }
    }
}
