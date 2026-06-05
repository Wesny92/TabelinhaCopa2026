package com.exemplo.copa2026.ui.grupo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.exemplo.copa2026.data.repository.GrupoRepository
import com.exemplo.copa2026.data.repository.PartidaRepository
import com.exemplo.copa2026.di.AppContainer
import com.exemplo.copa2026.domain.usecase.CalcularClassificacaoUseCase
import com.exemplo.copa2026.ui.grupo.components.PartidaItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GrupoViewModel(
    private val grupoId: Int,
    private val grupoRepository: GrupoRepository,
    private val partidaRepository: PartidaRepository,
    private val calcularClassificacao: CalcularClassificacaoUseCase,
    private val container: AppContainer
) : ViewModel() {

    data class DialogState(
        val partidaId: Int = 0,
        val timeCasaNome: String = "",
        val timeForaNome: String = "",
        val golsCasa: String = "",
        val golsFora: String = ""
    )

    data class GrupoUiState(
        val grupoLetra: String = "",
        val tabela: List<com.exemplo.copa2026.domain.model.ClassificacaoTime> = emptyList(),
        val partidas: List<PartidaItem> = emptyList(),
        val dialog: DialogState? = null,
        val isLoading: Boolean = true
    )

    private val _uiState = MutableStateFlow(GrupoUiState())
    val uiState: StateFlow<GrupoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            grupoRepository.getGrupoComTudo(grupoId).collect { grupoComTudo ->
                val tabela = calcularClassificacao(
                    grupoComTudo.times,
                    grupoComTudo.partidas
                )
                val partidasItems = grupoComTudo.partidas.map { partida ->
                    val casa = grupoComTudo.times.first { it.id == partida.timeCasaId }
                    val fora = grupoComTudo.times.first { it.id == partida.timeForaId }
                    PartidaItem(
                        partidaId = partida.id,
                        rodada = partida.rodada,
                        timeCasaEmoji = casa.bandeiraEmoji,
                        timeCasaNome = casa.nome,
                        timeForaNome = fora.nome,
                        timeForaEmoji = fora.bandeiraEmoji,
                        golsCasa = partida.golsCasa,
                        golsFora = partida.golsFora,
                        realizado = partida.realizado,
                        data = partida.data,
                        horario = partida.horario,
                        estadio = partida.estadio,
                        cidade = partida.cidade,
                        pais = partida.pais
                    )
                }.sortedBy { it.rodada }

                _uiState.update {
                    it.copy(
                        grupoLetra = grupoComTudo.grupo.letra,
                        tabela = tabela,
                        partidas = partidasItems,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun abrirDialogPlacar(partidaId: Int) {
        val partida = _uiState.value.partidas.first { it.partidaId == partidaId }
        _uiState.update {
            it.copy(
                dialog = DialogState(
                    partidaId = partidaId,
                    timeCasaNome = partida.timeCasaNome,
                    timeForaNome = partida.timeForaNome,
                    golsCasa = partida.golsCasa?.toString() ?: "",
                    golsFora = partida.golsFora?.toString() ?: ""
                )
            )
        }
    }

    fun fecharDialog() {
        _uiState.update { it.copy(dialog = null) }
    }

    fun atualizarGolsCasa(valor: String) {
        _uiState.update {
            it.copy(dialog = it.dialog?.copy(golsCasa = valor))
        }
    }

    fun atualizarGolsFora(valor: String) {
        _uiState.update {
            it.copy(dialog = it.dialog?.copy(golsFora = valor))
        }
    }

    fun salvarResultado() {
        val dialog = _uiState.value.dialog ?: return
        val gc = dialog.golsCasa.toIntOrNull() ?: return
        val gf = dialog.golsFora.toIntOrNull() ?: return

        viewModelScope.launch {
            partidaRepository.salvarResultado(dialog.partidaId, gc, gf)
            fecharDialog()
            // Preenche o mata-mata com grupos ja finalizados (parcial)
            val todosGrupos = grupoRepository.getAllGruposComTudo().first()
            container.montarChaveamentoUseCase.preencherRound32(
                todosGrupos = todosGrupos,
                calcularClassificacao = container.calcularClassificacaoUseCase,
                selecionarTerceiros = container.selecionarMelhoresTerceirosUseCase
            )
        }
    }
}

class GrupoViewModelFactory(
    private val grupoId: Int,
    private val container: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GrupoViewModel(
            grupoId = grupoId,
            grupoRepository = container.grupoRepository,
            partidaRepository = container.partidaRepository,
            calcularClassificacao = container.calcularClassificacaoUseCase,
            container = container
        ) as T
    }
}
