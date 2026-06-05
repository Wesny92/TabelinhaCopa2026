package com.exemplo.copa2026.domain.usecase

import com.exemplo.copa2026.data.model.GrupoComTudo
import com.exemplo.copa2026.domain.model.TerceiroColocado

class SelecionarMelhoresTerceirosUseCase(
    private val calcularClassificacao: CalcularClassificacaoUseCase
) {

    operator fun invoke(todosGrupos: List<GrupoComTudo>): List<TerceiroColocado> {
        val terceiros = todosGrupos.map { grupo ->
            val tabela = calcularClassificacao(grupo.times, grupo.partidas)
            val terceiro = tabela[2]
            TerceiroColocado(
                time = terceiro.time,
                grupoLetra = grupo.grupo.letra,
                classificacao = terceiro
            )
        }

        val ordenados = terceiros.sortedWith(
            compareByDescending<TerceiroColocado> { it.classificacao.pontos }
                .thenByDescending { it.classificacao.saldoGols }
                .thenByDescending { it.classificacao.golsPro }
        )

        return ordenados.mapIndexed { index, terceiro ->
            terceiro.copy(classificado = index < 8)
        }
    }
}
