package com.exemplo.copa2026.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Tour
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exemplo.copa2026.di.AppContainer
import com.exemplo.copa2026.ui.theme.FifaBlueBg
import com.exemplo.copa2026.ui.theme.FifaDark
import com.exemplo.copa2026.ui.theme.FifaGold
import com.exemplo.copa2026.ui.theme.FifaNavy
import com.exemplo.copa2026.ui.theme.FWC2026Font

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    container: AppContainer,
    onGrupoClick: (Int) -> Unit,
    onMataMataClick: () -> Unit,
    onTerceirosClick: () -> Unit,
    onCalendarioClick: () -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = com.exemplo.copa2026.di.HomeViewModelFactory(container)
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
                    // Linhas diagonais estilo FIFA
                    val goldLine = FifaGold.copy(alpha = 0.12f)
                    val step = 55f
                    var x = -h
                    while (x < w + h) {
                        drawLine(goldLine, Offset(x, h), Offset(x + h, 0f), strokeWidth = 1.5f)
                        x += step
                    }
                    // Losangos/diamantes geometricos
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
                    // Borda inferior dourada
                    drawLine(FifaGold.copy(alpha = 0.3f), Offset(0f, h - 2f), Offset(w, h - 2f), strokeWidth = 2f)
                }
                Box(
                    modifier = Modifier.fillMaxSize().padding(top = 28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Copa do Mundo 2026",
                        fontFamily = FWC2026Font,
                        fontWeight = FontWeight.Black,
                        color = FifaGold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    drawRect(Color(0xFF1A1A2E))
                    // Linhas diagonais douradas
                    val goldLine = FifaGold.copy(alpha = 0.10f)
                    val step = 45f
                    var x = -h
                    while (x < w + h) {
                        drawLine(goldLine, Offset(x, 0f), Offset(x + h, h), strokeWidth = 1f)
                        x += step
                    }
                    // Diamantes geometricos
                    val greenLine = Color(0xFF4CAF50).copy(alpha = 0.12f)
                    val dW = 50f
                    val dH = 16f
                    for (col in 0..((w / dW).toInt() + 1)) {
                        for (row in 0..((h / dH).toInt() + 1)) {
                            val cx = (col * dW) + (if (row % 2 == 0) 0f else dW / 2f)
                            val cy = row * dH
                            val path = Path().apply {
                                moveTo(cx, cy - dH / 2f)
                                lineTo(cx + dW / 4f, cy)
                                lineTo(cx, cy + dH / 2f)
                                lineTo(cx - dW / 4f, cy)
                                close()
                            }
                            drawPath(path, greenLine, style = Stroke(1f))
                        }
                    }
                    // Borda superior dourada
                    drawLine(FifaGold.copy(alpha = 0.25f), Offset(0f, 1f), Offset(w, 1f), strokeWidth = 1.5f)
                }
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = false, onClick = onCalendarioClick,
                        icon = { Icon(Icons.Outlined.CalendarMonth, "Calendário", tint = FifaGold) },
                        label = { Text("Calendário", color = Color.White) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.White.copy(alpha = 0.10f)
                        )
                    )
                    NavigationBarItem(
                        selected = false, onClick = onMataMataClick,
                        icon = { Icon(Icons.Outlined.Tour, "Chaveamento", tint = FifaGold) },
                        label = { Text("Chaveamento", color = Color.White) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.White.copy(alpha = 0.10f)
                        )
                    )
                    NavigationBarItem(
                        selected = false, onClick = onTerceirosClick,
                        icon = { Icon(Icons.Outlined.EmojiEvents, "3os Colocados", tint = FifaGold) },
                        label = { Text("3os Colocados", color = Color.White) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.White.copy(alpha = 0.10f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(FifaNavy, FifaBlueBg, FifaBlueBg)
                    )
                )
        ) {
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(uiState.grupos, key = { it.grupoId }) { grupo ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().clickable { onGrupoClick(grupo.grupoId) },
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Grupo ${grupo.letra}",
                                    fontFamily = FWC2026Font,
                                    fontWeight = FontWeight.Black,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = FifaGold)
                                Text("${grupo.jogosRealizados}/${grupo.totalJogos}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { if (grupo.totalJogos > 0) grupo.jogosRealizados.toFloat() / grupo.totalJogos else 0f },
                                modifier = Modifier.fillMaxWidth().height(3.dp),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                            Spacer(Modifier.height(8.dp))
                            grupo.times.forEachIndexed { i, nome ->
                                Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text(grupo.bandeiras.getOrElse(i) { "" })
                                    Spacer(Modifier.width(6.dp))
                                    Text(nome, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }
                }
            }
        }
        }
    }
}
