package com.gowoon.donmani_android.navigation

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gowoon.common.di.FeatureJson
import com.gowoon.donmani_android.SplashScreen
import com.gowoon.home.navigation.homeNavigationRoute
import com.gowoon.home.navigation.homeScreen
import com.gowoon.home.navigation.navigateToHome
import com.gowoon.record.navigation.navigateToRecord
import com.gowoon.record.navigation.navigateToRecordInput
import com.gowoon.record.navigation.recordGraph
import com.gowoon.recordlist.navigation.navigateToRecordList
import com.gowoon.recordlist.navigation.recordListNavigationRoute
import com.gowoon.recordlist.navigation.recordListScreen
import com.gowoon.setting.navigation.navigateToSetting
import com.gowoon.setting.navigation.settingScreen
import com.gowoon.ui.util.rememberHiltJson

// TODO enter, exit transition
internal const val splashNavigationRoute = "splash_route"

@Composable
fun DonmaniNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    @FeatureJson
    val json = rememberHiltJson()
    val context = LocalContext.current

    val startDestination = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        splashNavigationRoute
    } else {
        homeNavigationRoute
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = splashNavigationRoute,
            enterTransition = {
                expandVertically(
                    expandFrom = Alignment.CenterVertically,
                    animationSpec = tween(1000)
                ) + fadeIn(animationSpec = tween(1000))
            }
        ) {
            SplashScreen {
                navController.navigateToHome(splashNavigationRoute)
            }
        }
        homeScreen(
            navigateToSetting = navController::navigateToSetting,
            navigateToCalendar = {},
            navigateToRecord = { hasToday, hasYesterday ->
                navController.navigateToRecord(
                    hasTodayRecord = hasToday,
                    hasYesterdayRecord = hasYesterday
                )
            },
            navigateToRecordList = navController::navigateToRecordList
        )
        recordGraph(
            onClickBack = navController::popBackStack,
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
            navigateToRecord = {
                navController.navigateToRecord(
                    from = recordListNavigationRoute,
                    hasTodayRecord = false,
                    hasYesterdayRecord = false
                )
            }
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