package com.gowoon.donmani_android.navigation

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.gowoon.common.di.FeatureJson
import com.gowoon.home.navigation.homeScreen
import com.gowoon.home.navigation.navigateToHome
import com.gowoon.motivation.navigation.motivationScreen
import com.gowoon.motivation.navigation.navigateToReward
import com.gowoon.onboarding.navigation.navigateToOnBoarding
import com.gowoon.onboarding.navigation.onBoardingScreen
import com.gowoon.record.navigation.navigateToRecord
import com.gowoon.record.navigation.navigateToRecordAndPopUpTo
import com.gowoon.record.navigation.navigateToRecordInput
import com.gowoon.record.navigation.recordGraph
import com.gowoon.recordlist.navigation.navigateToRecordList
import com.gowoon.recordlist.navigation.recordListScreen
import com.gowoon.setting.navigation.navigateToSetting
import com.gowoon.setting.navigation.settingScreen
import com.gowoon.splash.navigation.SplashNavigationRoute
import com.gowoon.splash.navigation.splashScreen
import com.gowoon.starbottlelist.navigation.navigateToStarBottle
import com.gowoon.starbottlelist.navigation.navigateToStarBottleList
import com.gowoon.starbottlelist.navigation.starBottleListScreen
import com.gowoon.statistics.navigation.navigateToStatistics
import com.gowoon.statistics.navigation.statisticsScreen
import com.gowoon.ui.util.rememberHiltJson

@Composable
fun DonmaniNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    isFromFcm: Boolean
) {
    @FeatureJson
    val json = rememberHiltJson()
    val context = LocalContext.current

    val startDestination = SplashNavigationRoute
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        splashScreen(
            navigateToHome = {
                navController.navigateToHome(
                    referrer = "launcher",
                    isFromFcm = isFromFcm
                )
            },
            navigateToOnBoarding = navController::navigateToOnBoarding
        )
        onBoardingScreen(
            navigateToHome = { navController.navigateToHome(referrer = "onboarding") },
            navigateToRecord = {
                navController.navigateToRecord(
                    hasTodayRecord = false,
                    hasYesterdayRecord = false,
                    fromStart = true,
                    referrer = "onboarding"
                )
            }
        )
        homeScreen(
            navigateToSetting = navController::navigateToSetting,
            navigateToReward = navController::navigateToReward,
            navigateToRecord = { hasToday, hasYesterday, referrer ->
                navController.navigateToRecord(
                    hasTodayRecord = hasToday,
                    hasYesterdayRecord = hasYesterday,
                    referrer = referrer
                )
            },
            navigateToRecordList = { records, year, month ->
                navController.navigateToRecordList(json.encodeToString(records), year, month)
            },
            navigateToStarBottleList = navController::navigateToStarBottleList
        )
        recordGraph(
            onClickBack = navController::popBackStack,
            navigateToHome = { navController.navigateToHome(referrer = null) },
            navigateToRecordInput = { type, screenType ->
                navController.navigateToRecordInput(type = type, screenType = screenType)
            },
            navigateToRecordInputWithData = { consumption, screenType ->
                navController.navigateToRecordInput(
                    consumption = json.encodeToString(consumption),
                    screenType = screenType
                )
            },
            popBackStackWithArgument = { key, data ->
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    key,
                    data
                )
                navController.popBackStack()
            }
        )
        recordListScreen(
            onClickBack = navController::popBackStack,
            navigateToRecord = {
                navController.navigateToRecordAndPopUpTo("recordhistory")
            },
            navigateToStatistics = navController::navigateToStatistics,
            navigateToStarBottleList = navController::navigateToStarBottleList
        )
        statisticsScreen(
            onClickBack = navController::popBackStack,
            navigateToWebView = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                context.startActivity(intent)
            }
        )
        starBottleListScreen(
            onClickBack = navController::popBackStack,
            navigateToStarBottle = navController::navigateToStarBottle,
            navigateToRecordList = { records, year, month ->
                navController.navigateToRecordList(json.encodeToString(records), year, month)
            }
        )
        settingScreen(
            onClickBack = navController::popBackStack,
            navigateToWebView = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                context.startActivity(intent)
            },
            navigateToSystemSetting = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${context.packageName}")
                context.startActivity(intent)
            }
        )
        motivationScreen()
    }
}