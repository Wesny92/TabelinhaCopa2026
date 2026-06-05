package com.exemplo.copa2026.ui.terceiros

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.exemplo.copa2026.data.repository.GrupoRepository
import com.exemplo.copa2026.di.AppContainer
import com.exemplo.copa2026.domain.model.TerceiroColocado
import com.exemplo.copa2026.domain.usecase.SelecionarMelhoresTerceirosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TerceirosViewModel(
    private val grupoRepository: GrupoRepository,
    private val selecionarTerceiros: SelecionarMelhoresTerceirosUseCase
) : ViewModel() {

    data class UiState(
        val terceiros: List<TerceiroColocado> = emptyList(),
        val isLoading: Boolean = true
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val gruposIds = 1..12
            val todosGrupos = gruposIds.map { id ->
                grupoRepository.getGrupoComTudo(id).first()
            }
            val resultado = selecionarTerceiros(todosGrupos)
            _uiState.update {
                it.copy(terceiros = resultado, isLoading = false)
            }
        }
    }
}

class TerceirosViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TerceirosViewModel(
            grupoRepository = container.grupoRepository,
            selecionarTerceiros = container.selecionarMelhoresTerceirosUseCase
        ) as T
    }
}
