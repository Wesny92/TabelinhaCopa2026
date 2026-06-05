package com.exemplo.copa2026.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.exemplo.copa2026.di.AppContainer
import com.exemplo.copa2026.ui.grupo.GrupoScreen
import com.exemplo.copa2026.ui.calendario.CalendarioScreen
import com.exemplo.copa2026.ui.home.HomeScreen
import com.exemplo.copa2026.ui.matamata.MataMataScreen
import com.exemplo.copa2026.ui.terceiros.TerceirosScreen

@Composable
fun CopaNavGraph(
    container: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Rotas.HOME
    ) {
        composable(Rotas.HOME) {
            HomeScreen(
                container = container,
                onGrupoClick = { grupoId ->
                    navController.navigate(Rotas.grupo(grupoId))
                },
                onMataMataClick = {
                    navController.navigate(Rotas.MATA_MATA)
                },
                onTerceirosClick = {
                    navController.navigate(Rotas.TERCEIROS)
                },
                onCalendarioClick = {
                    navController.navigate(Rotas.CALENDARIO)
                }
            )
        }

        composable(
            route = Rotas.GRUPO,
            arguments = listOf(
                navArgument("grupoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val grupoId = backStackEntry.arguments?.getInt("grupoId") ?: 1
            GrupoScreen(
                grupoId = grupoId,
                container = container,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Rotas.MATA_MATA) {
            MataMataScreen(
                container = container,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Rotas.TERCEIROS) {
            TerceirosScreen(
                container = container,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Rotas.CALENDARIO) {
            CalendarioScreen(
                container = container,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
