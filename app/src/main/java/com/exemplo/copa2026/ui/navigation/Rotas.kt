package com.exemplo.copa2026.ui.navigation

object Rotas {
    const val HOME = "home"
    const val GRUPO = "grupo/{grupoId}"
    const val MATA_MATA = "mata_mata"
    const val TERCEIROS = "terceiros"
    const val CALENDARIO = "calendario"

    fun grupo(grupoId: Int) = "grupo/$grupoId"
}
