package com.exemplo.copa2026.ui.matamata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.exemplo.copa2026.data.local.dao.GrupoDao
import com.exemplo.copa2026.data.local.dao.TimeDao
import com.exemplo.copa2026.data.repository.MataMataRepository
import com.exemplo.copa2026.di.AppContainer
import com.exemplo.copa2026.domain.model.ConfrontoChave
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MataMataViewModel(
    private val mataMataRepository: MataMataRepository,
    private val timeDao: TimeDao,
    private val grupoDao: GrupoDao,
    private val container: AppContainer
) : ViewModel() {

    data class UiState(
        val faseSelecionada: String = "R32",
        val confrontos: List<ConfrontoChave> = emptyList(),
        val todasFases: Map<String, List<ConfrontoChave>> = emptyMap(),
        val isLoading: Boolean = true
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val grupoMap = mutableMapOf<Int, String>()

    init {
        viewModelScope.launch {
            val grupos = grupoDao.getAllGrupos().first()
            grupoMap.clear()
            grupos.forEach { grupoMap[it.id] = it.letra }
        }
        carregarFase("R32")
        carregarTodasFases()
    }

    fun selecionarFase(fase: String) {
        carregarFase(fase)
    }

    private fun makeConfronto(tc: com.exemplo.copa2026.data.model.Time?, tf: com.exemplo.copa2026.data.model.Time?,
                               gc: Int?, gf: Int?, partida: com.exemplo.copa2026.data.model.PartidaMataMata) =
        ConfrontoChave(
            partida = partida, timeCasa = tc, timeFora = tf,
            grupoCasa = tc?.grupoId?.let { grupoMap[it] },
            grupoFora = tf?.grupoId?.let { grupoMap[it] },
            golsCasa = gc, golsFora = gf,
            vencedor = when {
                gc != null && gf != null && gc > gf -> tc
                gc != null && gf != null && gf > gc -> tf
                else -> null
            }
        )

    private fun carregarTodasFases() {
        viewModelScope.launch {
            combine(
                mataMataRepository.getAllPartidasMataMata(),
                timeDao.getAllTimes()
            ) { partidas, times ->
                val timeMap = times.associateBy { it.id }
                val fases = listOf("R32", "OITAVAS", "QUARTAS", "SEMI", "TERCEIRO", "FINAL")
                val mapa = mutableMapOf<String, List<ConfrontoChave>>()
                for (fase in fases) {
                    mapa[fase] = partidas.filter { it.fase == fase }.map { partida ->
                        val tc = partida.timeCasaId?.let { timeMap[it] }
                        val tf = partida.timeForaId?.let { timeMap[it] }
                        makeConfronto(tc, tf, partida.golsCasa, partida.golsFora, partida)
                    }
                }
                _uiState.update { it.copy(todasFases = mapa) }
            }.collect { }
        }
    }

    private fun carregarFase(fase: String) {
        viewModelScope.launch {
            combine(
                mataMataRepository.getPartidasByFase(fase),
                timeDao.getAllTimes()
            ) { partidas, times ->
                val timeMap = times.associateBy { it.id }
                partidas.map { partida ->
                    val timeCasa = partida.timeCasaId?.let { timeMap[it] }
                    val timeFora = partida.timeForaId?.let { timeMap[it] }
                    makeConfronto(timeCasa, timeFora, partida.golsCasa, partida.golsFora, partida)
                }
            }.collect { confrontos ->
                _uiState.update {
                    it.copy(
                        faseSelecionada = fase,
                        confrontos = confrontos,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun salvarResultado(partidaId: Int, golsCasa: Int, golsFora: Int) {
        viewModelScope.launch {
            mataMataRepository.salvarResultadoMataMata(partidaId, golsCasa, golsFora)
            val partida = mataMataRepository.getPartidaMataMataById(partidaId).first()
            if (partida != null) {
                container.montarChaveamentoUseCase.propagarVencedor(partida)
            }
        }
    }
}

class MataMataViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MataMataViewModel(
            mataMataRepository = container.mataMataRepository,
            timeDao = container.database.timeDao(),
            grupoDao = container.database.grupoDao(),
            container = container
        ) as T
    }
}
