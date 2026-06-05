package com.exemplo.copa2026.ui.calendario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.exemplo.copa2026.data.local.dao.GrupoDao
import com.exemplo.copa2026.data.local.dao.PartidaDao
import com.exemplo.copa2026.data.local.dao.PartidaMataMataDao
import com.exemplo.copa2026.data.local.dao.TimeDao
import com.exemplo.copa2026.data.model.PartidaMataMata
import com.exemplo.copa2026.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalendarioViewModel(
    private val partidaDao: PartidaDao,
    private val mataMataDao: PartidaMataMataDao,
    timeDao: TimeDao,
    grupoDao: GrupoDao
) : ViewModel() {

    data class JogoCalendario(
        val id: Int,
        val grupoLetra: String,
        val emojiCasa: String,
        val nomeCasa: String,
        val emojiFora: String,
        val nomeFora: String,
        val data: String,
        val horario: String,
        val estadio: String,
        val cidade: String,
        val pais: String,
        val fase: String = "",
        val transmissao: String = ""
    )

    data class UiState(
        val jogos: List<JogoCalendario> = emptyList(),
        val isLoading: Boolean = true
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                partidaDao.getAllPartidas(),
                mataMataDao.getAllPartidasMataMata(),
                timeDao.getAllTimes(),
                grupoDao.getAllGrupos()
            ) { partidas, mataMata, times, grupos ->
                val timeMap = times.associateBy { it.id }
                val grupoMap = grupos.associateBy { it.id }

                val jogosGrupos = partidas.map { p ->
                    val casa = timeMap[p.timeCasaId] ?: return@map null
                    val fora = timeMap[p.timeForaId] ?: return@map null
                    val letra = grupoMap[p.grupoId]?.letra ?: ""
                    JogoCalendario(
                        id = p.id, grupoLetra = letra,
                        emojiCasa = casa.bandeiraEmoji, nomeCasa = casa.nome,
                        emojiFora = fora.bandeiraEmoji, nomeFora = fora.nome,
                        data = p.data, horario = p.horario,
                        estadio = p.estadio, cidade = p.cidade, pais = p.pais,
                        transmissao = p.transmissao
                    )
                }.filterNotNull()

                val nomesFases = mapOf(
                    "R32" to "16 avos", "OITAVAS" to "Oitavas",
                    "QUARTAS" to "Quartas", "SEMI" to "Semifinal",
                    "TERCEIRO" to "3 Lugar", "FINAL" to "Final"
                )

                val jogosMataMata = mataMata.map { mm ->
                    val casa = mm.timeCasaId?.let { timeMap[it] }
                    val fora = mm.timeForaId?.let { timeMap[it] }
                    val faseNome = nomesFases[mm.fase] ?: mm.fase
                    JogoCalendario(
                        id = mm.id, grupoLetra = faseNome,
                        emojiCasa = casa?.bandeiraEmoji ?: "",
                        nomeCasa = casa?.nome ?: "A definir",
                        emojiFora = fora?.bandeiraEmoji ?: "",
                        nomeFora = fora?.nome ?: "A definir",
                        data = mm.data, horario = mm.horario,
                        estadio = mm.estadio, cidade = mm.cidade, pais = mm.pais,
                        fase = mm.fase,
                        transmissao = mm.transmissao
                    )
                }

                jogosGrupos + jogosMataMata
            }.collect { jogos ->
                _uiState.update { it.copy(jogos = jogos, isLoading = false) }
            }
        }
    }
}

class CalendarioViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalendarioViewModel(
            container.database.partidaDao(),
            container.database.partidaMataMataDao(),
            container.database.timeDao(),
            container.database.grupoDao()
        ) as T
    }
}
