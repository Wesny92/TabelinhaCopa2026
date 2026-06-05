package com.exemplo.copa2026.ui.grupo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ItemPartida(
    timeCasaEmoji: String,
    timeCasaNome: String,
    timeForaNome: String,
    timeForaEmoji: String,
    golsCasa: Int?,
    golsFora: Int?,
    realizado: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(timeCasaEmoji)
            Text(
                timeCasaNome,
                Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                if (realizado) "$golsCasa   x   $golsFora" else "?  x  ?",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                timeForaNome,
                Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall,
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )
            Text(timeForaEmoji)
        }
    }
}
