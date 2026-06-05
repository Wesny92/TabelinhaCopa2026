package com.exemplo.copa2026.domain.usecase

import com.exemplo.copa2026.data.model.Partida
import com.exemplo.copa2026.data.model.Time
import com.exemplo.copa2026.domain.model.ClassificacaoTime

class CalcularClassificacaoUseCase {

    operator fun invoke(
        times: List<Time>,
        partidas: List<Partida>
    ): List<ClassificacaoTime> {
        val mapa = times.associate { time ->
            time.id to ClassificacaoTime(time = time)
        }.toMutableMap()

        for (p in partidas) {
            if (!p.realizado || p.golsCasa == null || p.golsFora == null) continue

            val gc = p.golsCasa
            val gf = p.golsFora
            val casa = mapa[p.timeCasaId] ?: continue
            val fora = mapa[p.timeForaId] ?: continue

            val novoCasa = casa.copy(
                jogos = casa.jogos + 1,
                golsPro = casa.golsPro + gc,
                golsContra = casa.golsContra + gf
            )
            val novoFora = fora.copy(
                jogos = fora.jogos + 1,
                golsPro = fora.golsPro + gf,
                golsContra = fora.golsContra + gc
            )

            when {
                gc > gf -> {
                    mapa[p.timeCasaId] = novoCasa.copy(
                        pontos = novoCasa.pontos + 3,
                        vitorias = novoCasa.vitorias + 1,
                        saldoGols = novoCasa.golsPro - novoCasa.golsContra
                    )
                    mapa[p.timeForaId] = novoFora.copy(
                        derrotas = novoFora.derrotas + 1,
                        saldoGols = novoFora.golsPro - novoFora.golsContra
                    )
                }
                gc < gf -> {
                    mapa[p.timeCasaId] = novoCasa.copy(
                        derrotas = novoCasa.derrotas + 1,
                        saldoGols = novoCasa.golsPro - novoCasa.golsContra
                    )
                    mapa[p.timeForaId] = novoFora.copy(
                        pontos = novoFora.pontos + 3,
                        vitorias = novoFora.vitorias + 1,
                        saldoGols = novoFora.golsPro - novoFora.golsContra
                    )
                }
                else -> {
                    mapa[p.timeCasaId] = novoCasa.copy(
                        pontos = novoCasa.pontos + 1,
                        empates = novoCasa.empates + 1,
                        saldoGols = novoCasa.golsPro - novoCasa.golsContra
                    )
                    mapa[p.timeForaId] = novoFora.copy(
                        pontos = novoFora.pontos + 1,
                        empates = novoFora.empates + 1,
                        saldoGols = novoFora.golsPro - novoFora.golsContra
                    )
                }
            }
        }

        return mapa.values.sortedWith(
            compareByDescending<ClassificacaoTime> { it.pontos }
                .thenByDescending { it.saldoGols }
                .thenByDescending { it.golsPro }
                .thenBy { it.time.nome }
        )
    }
}
