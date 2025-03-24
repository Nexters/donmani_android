package com.gowoon.donmani_android.navigation

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.gowoon.common.di.FeatureJson
import com.gowoon.home.navigation.homeNavigationRoute
import com.gowoon.home.navigation.homeScreen
import com.gowoon.home.navigation.navigateToHome
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
import com.gowoon.ui.util.rememberHiltJson

@Composable
fun DonmaniNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    @FeatureJson
    val json = rememberHiltJson()
    val context = LocalContext.current

    val startDestination = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        SplashNavigationRoute
    } else {
        homeNavigationRoute
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        splashScreen(
            navigateToHome = navController::navigateToHome,
            navigateToOnBoarding = navController::navigateToOnBoarding
        )
        onBoardingScreen(
            navigateToHome = navController::navigateToHome,
            navigateToRecord = {
                navController.navigateToRecord(
                    hasTodayRecord = false,
                    hasYesterdayRecord = false,
                    fromStart = true
                )
            }
        )
        homeScreen(
            navigateToSetting = navController::navigateToSetting,
            navigateToRecord = { hasToday, hasYesterday ->
                navController.navigateToRecord(
                    hasTodayRecord = hasToday,
                    hasYesterdayRecord = hasYesterday
                )
            },
            navigateToRecordList = { records ->
                navController.navigateToRecordList(json.encodeToString(records))
            }
        )
        recordGraph(
            onClickBack = navController::popBackStack,
            navigateToHome = navController::navigateToHome,
            navigateToRecordInput = {
                navController.navigateToRecordInput(type = it)
            },
            navigateToRecordInputWithData = {
                navController.navigateToRecordInput(consumption = json.encodeToString(it))
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
            navigateToRecord = navController::navigateToRecordAndPopUpTo
        )
        settingScreen(
            onClickBack = navController::popBackStack,
            navigateToWebView = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                context.startActivity(intent)
            }
        )
    }
}