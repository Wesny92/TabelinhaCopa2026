package com.exemplo.copa2026.ui.matamata

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exemplo.copa2026.di.AppContainer
import com.exemplo.copa2026.domain.model.ConfrontoChave
import com.exemplo.copa2026.ui.theme.FifaDark
import com.exemplo.copa2026.ui.theme.FifaGold
import com.exemplo.copa2026.ui.theme.FWC2026Font
import com.exemplo.copa2026.ui.theme.GreenLight
import com.exemplo.copa2026.ui.theme.GreenPrimary
import kotlinx.coroutines.launch

// FIFA match label: "J73" for R32, "J89" for Oitavas, etc.
private fun jogoLabel(confronto: ConfrontoChave): String {
    val n = confronto.partida.numeroJogo
    return when (confronto.partida.fase) {
        "R32" -> "J${n + 72}"
        "OITAVAS" -> "J${n + 88}"
        "QUARTAS" -> "J${n + 96}"
        "SEMI" -> "J${n + 100}"
        "TERCEIRO" -> "J103"
        "FINAL" -> "J104"
        else -> "J$n"
    }
}

// Regulamento FIFA 12.6: cada slot R32 tem origem fixa
private fun slotOrigem(numeroJogo: Int, isCasa: Boolean): String? {
    return when (numeroJogo) {
        1 -> if (isCasa) "2A" else "2B"
        2 -> if (isCasa) "1E" else "3° ABCDF"
        3 -> if (isCasa) "1F" else "2C"
        4 -> if (isCasa) "1C" else "2F"
        5 -> if (isCasa) "1I" else "3° CDFGH"
        6 -> if (isCasa) "2E" else "2I"
        7 -> if (isCasa) "1A" else "3° CEFHI"
        8 -> if (isCasa) "1L" else "3° EHIJK"
        9 -> if (isCasa) "1D" else "3° BEFIJ"
        10 -> if (isCasa) "1G" else "3° AEHIJ"
        11 -> if (isCasa) "2K" else "2L"
        12 -> if (isCasa) "1H" else "2J"
        13 -> if (isCasa) "1B" else "3° EFGIJ"
        14 -> if (isCasa) "1J" else "2H"
        15 -> if (isCasa) "1K" else "3° DEIJL"
        16 -> if (isCasa) "2D" else "2G"
        else -> null
    }
}

// Oitavas, Quartas, Semis: V (Vencedor). TERCEIRO: D (Derrotado)
private fun origemLabel(c: ConfrontoChave): String {
    val oCasa = c.partida.partidaOrigemCasa
    val oFora = c.partida.partidaOrigemFora
    if (oCasa == null || oFora == null) return ""
    val njCasa = oCasa - 28
    val njFora = oFora - 28
    val p = if (c.partida.fase == "TERCEIRO") "D" else "V"
    return "${p}$njCasa × ${p}$njFora"
}

private fun formatGrupo(confronto: ConfrontoChave, isCasa: Boolean): String? {
    val nj = confronto.partida.numeroJogo
    if (confronto.partida.fase == "R32") {
        return slotOrigem(nj, isCasa)
    }
    // For later phases: return origem only for the first call (isCasa=false)
    if (isCasa) return null
    val orig = origemLabel(confronto)
    if (orig.isNotEmpty()) return orig
    val grupoLetra = if (isCasa) confronto.grupoCasa else confronto.grupoFora
    if (grupoLetra != null && grupoLetra.isNotEmpty()) return "Gr.$grupoLetra"
    return null
}

// R32: "1A × 2B" (two labels). Later phases: "W74 × W77" (one centered label)
private fun labelsLinha(confronto: ConfrontoChave, grpCasa: String, grpFora: String): String {
    if (confronto.partida.fase == "R32") {
        return if (grpCasa.isNotEmpty() && grpFora.isNotEmpty()) "$grpCasa  ×  $grpFora" else grpCasa + grpFora
    }
    return grpFora // already contains "W74 × W77"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MataMataScreen(
    container: AppContainer,
    onBack: () -> Unit,
    viewModel: MataMataViewModel = viewModel(
        factory = MataMataViewModelFactory(container)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var mainTab by remember { mutableStateOf(0) }

    var dialogConfronto by remember { mutableStateOf<ConfrontoChave?>(null) }
    var golsCasa by remember { mutableStateOf("") }
    var golsFora by remember { mutableStateOf("") }

    // Penalty shootout dialog state
    var dialogPenaltis by remember { mutableStateOf<Pair<ConfrontoChave, Pair<Int, Int>>?>(null) }

    val fases = listOf("R32", "OITAVAS", "QUARTAS", "SEMI", "TERCEIRO", "FINAL")
    val nomesFases = listOf("Round of 32", "Oitavas", "Quartas", "Semis", "3 Lugar", "Final")

    if (dialogConfronto != null) {
        val confronto = dialogConfronto!!
        AlertDialog(
            onDismissRequest = { dialogConfronto = null },
            title = {
                Text(
                    text = "${confronto.timeCasa?.nome ?: "A definir"} x ${confronto.timeFora?.nome ?: "A definir"}"
                )
            },
            text = {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = golsCasa,
                        onValueChange = { golsCasa = it.filter { c -> c.isDigit() } },
                        label = { Text(confronto.timeCasa?.nome?.take(6) ?: "Casa") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Text("  x  ", Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = golsFora,
                        onValueChange = { golsFora = it.filter { c -> c.isDigit() } },
                        label = { Text(confronto.timeFora?.nome?.take(6) ?: "Fora") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val gc = golsCasa.toIntOrNull() ?: return@Button
                    val gf = golsFora.toIntOrNull() ?: return@Button
                    if (gc == gf) {
                        // Draw: ask who won on penalties
                        dialogConfronto = null
                        dialogPenaltis = Pair(confronto, Pair(gc, gf))
                    } else {
                        viewModel.salvarResultado(confronto.partida.id, gc, gf)
                        dialogConfronto = null
                    }
                }) { Text("Salvar") }
            },
            dismissButton = {
                TextButton(onClick = { dialogConfronto = null }) { Text("Cancelar") }
            }
        )
    }

    // Penalty dialog
    if (dialogPenaltis != null) {
        val (confronto, scores) = dialogPenaltis!!
        val (gc, gf) = scores
        AlertDialog(
            onDismissRequest = { dialogPenaltis = null },
            title = {
                Text("${gc} x ${gf} - Quem venceu nos penaltis?")
            },
            text = {
                Column {
                    Button(
                        onClick = {
                            viewModel.salvarResultado(confronto.partida.id, gc + 1, gf)
                            dialogPenaltis = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${confronto.timeCasa?.bandeiraEmoji ?: ""} ${confronto.timeCasa?.nome ?: "Casa"}")
                    }
                    Button(
                        onClick = {
                            viewModel.salvarResultado(confronto.partida.id, gc, gf + 1)
                            dialogPenaltis = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${confronto.timeFora?.bandeiraEmoji ?: ""} ${confronto.timeFora?.nome ?: "Fora"}")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { dialogPenaltis = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            Column {
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
                        "Chaveamento",
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
            // Green tabs bar - colada ao azul
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenLight)
                    .border(2.dp, FifaGold)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.weight(1f).background(Color.Transparent)
                            .clickable { mainTab = 0 }.padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Chave", fontFamily = FWC2026Font, fontWeight = FontWeight.Black,
                            fontSize = 14.sp, color = FifaGold)
                    }
                    Box(Modifier.width(2.dp).fillMaxHeight().background(FifaGold))
                    Box(
                        modifier = Modifier.weight(1f).background(Color.Transparent)
                            .clickable { mainTab = 1 }.padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Bracket", fontFamily = FWC2026Font, fontWeight = FontWeight.Black,
                            fontSize = 14.sp, color = FifaGold)
                    }
                }
            }
        }
    }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {

            if (mainTab == 0) {
                // Chave tab: existing phase pager
                val pagerState = rememberPagerState(initialPage = 0) { fases.size }
                val scope = rememberCoroutineScope()

                LaunchedEffect(pagerState.currentPage) {
                    viewModel.selecionarFase(fases[pagerState.currentPage])
                }

                ScrollableTabRow(selectedTabIndex = pagerState.currentPage) {
                    fases.forEachIndexed { index, fase ->
                        Tab(selected = pagerState.currentPage == index,
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                            text = { Text(nomesFases[index], style = MaterialTheme.typography.bodySmall) })
                    }
                }

                if (uiState.isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)
                        ) {
                            items(uiState.confrontos) { confronto ->
                                CardMataMata(confronto = confronto, onClick = {
                                    dialogConfronto = confronto
                                    golsCasa = confronto.golsCasa?.toString() ?: ""
                                    golsFora = confronto.golsFora?.toString() ?: ""
                                })
                            }
                        }
                    }
                }
            } else {
                // Bracket tab
                if (uiState.todasFases.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    BracketView(todasFases = uiState.todasFases)
                }
            }
        }
    }
}

@Composable
private fun CardMataMata(
    confronto: ConfrontoChave,
    onClick: () -> Unit
) {
    val timeCasaNome = confronto.timeCasa?.nome ?: ""
    val timeForaNome = confronto.timeFora?.nome ?: ""
    val emojiCasa = confronto.timeCasa?.bandeiraEmoji ?: ""
    val emojiFora = confronto.timeFora?.bandeiraEmoji ?: ""
    val grpCasa = formatGrupo(confronto, true) ?: ""
    val grpFora = formatGrupo(confronto, false) ?: ""
    val bothUndefined = timeCasaNome.isEmpty() && timeForaNome.isEmpty()
    val showTeams = !bothUndefined

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LEFT: J number - big, bold, black. Smaller for 3-digit (J100+)
            val numeroJogo = jogoLabel(confronto)
            val fontSizeNum = if (numeroJogo.length > 3) 14.sp else 16.sp
            val widthNum = if (numeroJogo.length > 3) 44.dp else 40.dp
            Text(
                numeroJogo,
                fontSize = fontSizeNum,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.85f),
                modifier = Modifier.width(widthNum).padding(end = 8.dp),
                textAlign = TextAlign.Center
            )

            // RIGHT: everything else centered
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Date + time
                Text(
                    "${confronto.partida.data} ${confronto.partida.horario}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                // Location
                Text(
                    "${confronto.partida.cidade}, ${confronto.partida.pais} - ${confronto.partida.estadio}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
                // FIFA labels
                val linhaLabel = labelsLinha(confronto, grpCasa, grpFora)
                if (linhaLabel.isNotEmpty()) {
                    Text(
                        linhaLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black.copy(alpha = 0.7f),
                        modifier = Modifier.padding(vertical = 2.dp),
                        textAlign = TextAlign.Center
                    )
                }
                // Team names
                if (showTeams) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(emojiCasa, style = MaterialTheme.typography.bodyLarge)
                        Text(timeCasaNome,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(horizontal = 4.dp))
                        Text("  x  ",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary)
                        Text(timeForaNome,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(horizontal = 4.dp))
                        Text(emojiFora, style = MaterialTheme.typography.bodyLarge)
                    }
                }
                // Score
                if (confronto.golsCasa != null && confronto.golsFora != null) {
                    Text(
                        "${confronto.golsCasa} x ${confronto.golsFora}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
