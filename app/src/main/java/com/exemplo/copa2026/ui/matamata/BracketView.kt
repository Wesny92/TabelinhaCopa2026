package com.exemplo.copa2026.ui.matamata

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.exemplo.copa2026.domain.model.ConfrontoChave

@Composable
fun BracketView(todasFases: Map<String, List<ConfrontoChave>>) {
    val ctx = LocalContext.current
    val html = remember(todasFases) { buildInlineHtml(ctx, todasFases) }

    val dataHash = todasFases.hashCode()

    Box(Modifier.fillMaxSize().horizontalScroll(rememberScrollState())) {
        key(dataHash) {
            AndroidView(
                factory = { c ->
                    WebView(c).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        webViewClient = WebViewClient()
                        setBackgroundColor(0xFF060E22.toInt())
                        loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                    }
                },
                modifier = Modifier.width(1580.dp).fillMaxHeight()
            )
        }
    }
}

private fun buildInlineHtml(ctx: android.content.Context, todasFases: Map<String, List<ConfrontoChave>>): String {
    val reactJs = readAsset(ctx, "react.min.js")
    val reactDomJs = readAsset(ctx, "react-dom.min.js")
    val babelJs = readAsset(ctx, "babel.min.js")
    var html = readAsset(ctx, "bracket.html")

    // Build INIT_R32 from actual database data, reordered for correct bracket visual pairing
    val ordemBracket = listOf(102, 105, 101, 103, 111, 112, 109, 110,
                               104, 106, 107, 108, 114, 116, 113, 115)
    val r32Map = (todasFases["R32"] ?: emptyList()).associateBy { it.partida.id }
    val r32 = ordemBracket.mapNotNull { r32Map[it] }
    val r32Items = (0 until 16).map { i ->
        val c = r32.getOrNull(i)
        val casa = c?.timeCasa?.nome ?: "TBD"
        val fora = c?.timeFora?.nome ?: "TBD"
        """["$casa","$fora"]"""
    }.joinToString(",")
    html = html.replace(
        """Array.from({length:16}, () => ["TBD","TBD"])""",
        "[$r32Items]"
    )

    // Embed JS inline
    html = html.replace(
        """<script src="react.min.js"></script>""",
        "<script>${reactJs}</script>"
    )
    html = html.replace(
        """<script src="react-dom.min.js"></script>""",
        "<script>${reactDomJs}</script>"
    )
    html = html.replace(
        """<script src="babel.min.js"></script>""",
        "<script>${babelJs}</script>"
    )
    return html
}

private fun readAsset(ctx: android.content.Context, name: String): String =
    ctx.assets.open(name).bufferedReader().use { it.readText() }
