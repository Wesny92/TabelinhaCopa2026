package com.exemplo.copa2026.ui.grupo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exemplo.copa2026.di.AppContainer
import com.exemplo.copa2026.ui.grupo.components.DialogPlacar
import com.exemplo.copa2026.ui.grupo.components.ListaPartidas
import com.exemplo.copa2026.ui.grupo.components.TabelaGrupo
import com.exemplo.copa2026.ui.theme.FifaDark
import com.exemplo.copa2026.ui.theme.FifaGold
import com.exemplo.copa2026.ui.theme.FWC2026Font

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrupoScreen(
    grupoId: Int,
    container: AppContainer,
    onBack: () -> Unit,
    viewModel: GrupoViewModel = viewModel(
        factory = GrupoViewModelFactory(grupoId, container)
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
                        "Grupo ${uiState.grupoLetra}",
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
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = FifaGold
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Classificação",
                    fontFamily = FWC2026Font,
                    fontWeight = FontWeight.Black,
                    color = FifaGold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                TabelaGrupo(tabela = uiState.tabela)
                Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                ListaPartidas(
                    partidas = uiState.partidas,
                    onPartidaClick = { viewModel.abrirDialogPlacar(it) }
                )
            }
        }

        uiState.dialog?.let { dialog ->
            DialogPlacar(
                timeCasaNome = dialog.timeCasaNome,
                timeForaNome = dialog.timeForaNome,
                golsCasa = dialog.golsCasa,
                golsFora = dialog.golsFora,
                onGolsCasaChange = viewModel::atualizarGolsCasa,
                onGolsForaChange = viewModel::atualizarGolsFora,
                onSalvar = viewModel::salvarResultado,
                onCancelar = viewModel::fecharDialog
            )
        }
    }
}
