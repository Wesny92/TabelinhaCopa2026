package com.exemplo.copa2026.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exemplo.copa2026.data.repository.GrupoRepository
import com.exemplo.copa2026.data.repository.PartidaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val grupoRepository: GrupoRepository,
    private val partidaRepository: PartidaRepository
) : ViewModel() {

    data class GrupoCardData(
        val grupoId: Int,
        val letra: String,
        val jogosRealizados: Int,
        val totalJogos: Int = 6,
        val bandeiras: List<String> = emptyList(),
        val times: List<String> = emptyList()
    )

    data class HomeUiState(
        val grupos: List<GrupoCardData> = emptyList(),
        val isLoading: Boolean = true
    )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            grupoRepository.getAllGruposComTudo().collect { gruposComTudo ->
                val cards = gruposComTudo.map { grupoComTudo ->
                    val realizadas = partidaRepository.countPartidasRealizadasByGrupo(grupoComTudo.grupo.id)
                    GrupoCardData(
                        grupoId = grupoComTudo.grupo.id,
                        letra = grupoComTudo.grupo.letra,
                        jogosRealizados = realizadas,
                        bandeiras = grupoComTudo.times.map { it.bandeiraEmoji },
                        times = grupoComTudo.times.map { it.nome }
                    )
                }
                _uiState.update {
                    it.copy(grupos = cards, isLoading = false)
                }
            }
        }
    }
}
