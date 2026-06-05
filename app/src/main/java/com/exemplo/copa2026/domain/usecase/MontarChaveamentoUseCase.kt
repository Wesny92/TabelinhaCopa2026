package com.exemplo.copa2026.domain.usecase

import com.exemplo.copa2026.data.model.GrupoComTudo
import com.exemplo.copa2026.data.model.PartidaMataMata
import com.exemplo.copa2026.data.model.Time
import com.exemplo.copa2026.data.repository.MataMataRepository
import com.exemplo.copa2026.domain.model.TerceiroColocado
import kotlinx.coroutines.flow.first

class MontarChaveamentoUseCase(
    private val mataMataRepository: MataMataRepository
) {

    /**
     * Preenche os 16 confrontos do Round of 32 seguindo o regulamento FIFA 2026 (artigo 12.6):
     * M73 = 2A vs 2B      M81 = 1D vs M3(BEFIJ)
     * M74 = 1E vs M3(ABCDF)  M82 = 1G vs M3(AEHIJ)
     * M75 = 1F vs 2C      M83 = 2K vs 2L
     * M76 = 1C vs 2F      M84 = 1H vs 2J
     * M77 = 1I vs M3(CDFGH)  M85 = 1B vs M3(EFGIJ)
     * M78 = 2E vs 2I      M86 = 1J vs 2H
     * M79 = 1A vs M3(CEFHI)  M87 = 1K vs M3(DEIJL)
     * M80 = 1L vs M3(EHIJK)  M88 = 2D vs 2G
     */
    suspend fun preencherRound32(
        todosGrupos: List<GrupoComTudo>,
        calcularClassificacao: CalcularClassificacaoUseCase,
        selecionarTerceiros: SelecionarMelhoresTerceirosUseCase
    ) {
        val classificados = mutableMapOf<String, Time>()
        val tercGrupo = mutableMapOf<Time, String>()

        // Só classifica grupos com 6 partidas realizadas
        for (grupo in todosGrupos) {
            val realizadas = grupo.partidas.count { it.realizado }
            if (realizadas < 6) continue
            val tabela = calcularClassificacao(grupo.times, grupo.partidas)
            classificados["1${grupo.grupo.letra}"] = tabela[0].time
            classificados["2${grupo.grupo.letra}"] = tabela[1].time
            tercGrupo[tabela[2].time] = grupo.grupo.letra
        }

        // Só calcula melhores terceiros se tiver grupos suficientes
        val todosTerceiros = if (tercGrupo.size >= 8) {
            selecionarTerceiros(todosGrupos)
                .filter { it.classificado }
                .sortedWith(compareByDescending<TerceiroColocado> { it.classificacao.pontos }
                    .thenByDescending { it.classificacao.saldoGols }
                    .thenByDescending { it.classificacao.golsPro })
        } else emptyList()

        // Atribui cada 3o colocado ao primeiro slot que aceita seu grupo
        data class Slot3(val pos: Int, val pool: Set<String>)
        val slots3 = listOf(
            Slot3(2, setOf("A","B","C","D","F")),   // M74
            Slot3(5, setOf("C","D","F","G","H")),   // M77
            Slot3(7, setOf("C","E","F","H","I")),   // M79
            Slot3(8, setOf("E","H","I","J","K")),   // M80
            Slot3(9, setOf("B","E","F","I","J")),   // M81
            Slot3(10, setOf("A","E","H","I","J")),  // M82
            Slot3(13, setOf("E","F","G","I","J")),  // M85
            Slot3(15, setOf("D","E","I","J","L"))   // M87
        )

        val tercUsados = mutableSetOf<Time>()
        val slotParaTime = mutableMapOf<Int, Time?>()

        for (slot in slots3) {
            val idx = todosTerceiros.indexOfFirst { t ->
                t.time !in tercUsados && tercGrupo[t.time] in slot.pool
            }
            if (idx >= 0) {
                slotParaTime[slot.pos] = todosTerceiros[idx].time
                tercUsados.add(todosTerceiros[idx].time)
            }
        }
        for (slot in slots3) {
            if (slot.pos !in slotParaTime) {
                val idx = todosTerceiros.indexOfFirst { it.time !in tercUsados }
                if (idx >= 0) {
                    slotParaTime[slot.pos] = todosTerceiros[idx].time
                    tercUsados.add(todosTerceiros[idx].time)
                }
            }
        }

        // 16 confrontos FIFA 12.6
        val confrontos = listOf(
            1 to ("2A" to "2B"),               // M73
            2 to ("1E" to slotParaTime[2]),     // M74
            3 to ("1F" to "2C"),               // M75
            4 to ("1C" to "2F"),               // M76
            5 to ("1I" to slotParaTime[5]),     // M77
            6 to ("2E" to "2I"),               // M78
            7 to ("1A" to slotParaTime[7]),     // M79
            8 to ("1L" to slotParaTime[8]),     // M80
            9 to ("1D" to slotParaTime[9]),     // M81
            10 to ("1G" to slotParaTime[10]),   // M82
            11 to ("2K" to "2L"),              // M83
            12 to ("1H" to "2J"),              // M84
            13 to ("1B" to slotParaTime[13]),   // M85
            14 to ("1J" to "2H"),              // M86
            15 to ("1K" to slotParaTime[15]),   // M87
            16 to ("2D" to "2G")               // M88
        )

        for ((pos, origens) in confrontos) {
            val timeCasaId = resolverTime(origens.first, classificados)?.id
            val timeForaId = resolverTime(origens.second, classificados)?.id
            // Sempre atualiza: se um time é null, mantém o slot vazio desse lado
            mataMataRepository.atualizarTimesConfronto(100 + pos, timeCasaId, timeForaId)
        }
    }

    /**
     * Propaga o resultado de uma partida para os confrontos seguintes.
     * Na Final: propaga o vencedor para a final.
     * Na Semi: propaga o vencedor para a final e o perdedor para o 3o lugar.
     */
    suspend fun propagarVencedor(partida: PartidaMataMata) {
        if (!partida.realizado || partida.golsCasa == null || partida.golsFora == null) return

        val vencedorId = when {
            partida.golsCasa > partida.golsFora -> partida.timeCasaId
            partida.golsFora > partida.golsCasa -> partida.timeForaId
            else -> partida.timeCasaId
        }

        val perdedorId = if (vencedorId == partida.timeCasaId)
            partida.timeForaId else partida.timeCasaId

        val todas = mataMataRepository.getAllPartidasMataMata().first()

        val proximas = todas.filter {
            it.partidaOrigemCasa == partida.id || it.partidaOrigemFora == partida.id
        }

        for (proxima in proximas) {
            // Decide se propaga vencedor (Final) ou perdedor (3o lugar)
            val idPropagar = if (proxima.fase == "TERCEIRO") perdedorId else vencedorId

            val tc = if (proxima.partidaOrigemCasa == partida.id) idPropagar else proxima.timeCasaId
            val tf = if (proxima.partidaOrigemFora == partida.id) idPropagar else proxima.timeForaId
            mataMataRepository.atualizarTimesConfronto(proxima.id, tc, tf)
        }
    }

    private fun resolverTime(
        origem: Any?,
        classificados: Map<String, Time>
    ): Time? {
        return when (origem) {
            is String -> classificados[origem]
            is Time -> origem
            else -> null
        }
    }
}
