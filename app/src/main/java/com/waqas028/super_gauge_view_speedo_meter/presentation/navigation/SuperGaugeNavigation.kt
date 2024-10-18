package com.waqas028.super_gauge_view_speedo_meter.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.waqas028.super_gauge_view_speedo_meter.presentation.screen.HomeScreen
import com.waqas028.super_gauge_view_speedo_meter.presentation.screen.SuperGauge1

@Composable
fun SuperGaugeNavigation(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {

        composable<Routes.HomeScreen>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            HomeScreen(navController)
        }

        composable<Routes.SuperGauge1>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            SuperGauge1()
        }
    }
}