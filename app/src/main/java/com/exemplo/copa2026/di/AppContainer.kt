package com.exemplo.copa2026.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exemplo.copa2026.data.local.AppDatabase
import com.exemplo.copa2026.data.repository.GrupoRepository
import com.exemplo.copa2026.data.repository.MataMataRepository
import com.exemplo.copa2026.data.repository.PartidaRepository
import com.exemplo.copa2026.domain.usecase.CalcularClassificacaoUseCase
import com.exemplo.copa2026.domain.usecase.MontarChaveamentoUseCase
import com.exemplo.copa2026.domain.usecase.SelecionarMelhoresTerceirosUseCase
import com.exemplo.copa2026.ui.home.HomeViewModel

class AppContainer(private val context: Context) {

    val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    private val grupoDao get() = database.grupoDao()
    private val timeDao get() = database.timeDao()
    private val partidaDao get() = database.partidaDao()
    private val partidaMataMataDao get() = database.partidaMataMataDao()

    val grupoRepository by lazy {
        GrupoRepository(grupoDao, timeDao, partidaDao)
    }
    val partidaRepository by lazy {
        PartidaRepository(partidaDao)
    }
    val mataMataRepository by lazy {
        MataMataRepository(partidaMataMataDao)
    }

    val calcularClassificacaoUseCase by lazy {
        CalcularClassificacaoUseCase()
    }
    val selecionarMelhoresTerceirosUseCase by lazy {
        SelecionarMelhoresTerceirosUseCase(calcularClassificacaoUseCase)
    }
    val montarChaveamentoUseCase by lazy {
        MontarChaveamentoUseCase(mataMataRepository)
    }
}

class HomeViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(
            grupoRepository = container.grupoRepository,
            partidaRepository = container.partidaRepository
        ) as T
    }
}
