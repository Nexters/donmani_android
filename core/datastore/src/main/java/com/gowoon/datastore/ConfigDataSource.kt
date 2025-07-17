package com.gowoon.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.gowoon.datastore.di.TooltipDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConfigDataSource @Inject constructor(
    @TooltipDataStore private val datastore: DataStore<Preferences>
) {
    companion object {
        private val NO_CONSUMPTION_TOOLTIP_KEY = booleanPreferencesKey("no_consumption_tooltip_key")
        private val ON_BOARDING_KEY = booleanPreferencesKey("onboarding_key")
        private val YESTERDAY_TOOLTIP_KEY =
            stringPreferencesKey("yesterday_tooltip_last_checked_key")
        private val STAR_BOTTLE_LIST_TOOLTIP = booleanPreferencesKey("star_bottle_list_tooltip_key")
        private val STAR_BOTTLE_LIST_BANNER = booleanPreferencesKey("star_bottle_list_banner_key")
        private val STAR_BOTTLE_OPEN_SHEET = intPreferencesKey("star_bottle_open_sheet_key")
//        private val BGM_PLAY_STATUS = booleanPreferencesKey("bgm_play_status_key")
    }

    fun getNoConsumptionTooltipState(): Flow<Boolean> = datastore.data.map { preference ->
        preference[NO_CONSUMPTION_TOOLTIP_KEY] ?: true
    }

    suspend fun setNoConsumptionTooltipState(state: Boolean) {
        datastore.edit { preference ->
            preference[NO_CONSUMPTION_TOOLTIP_KEY] = state
        }
    }

    suspend fun getOnBoardingState(): Boolean = datastore.data.first()[ON_BOARDING_KEY] ?: true

    suspend fun setOnBoardingState(state: Boolean) {
        datastore.edit { preference ->
            preference[ON_BOARDING_KEY] = state
        }
    }

    fun getYesterdayTooltipLastCheckedDay(): Flow<String> = datastore.data.map { preference ->
        preference[YESTERDAY_TOOLTIP_KEY] ?: ""
    }

    suspend fun setYesterdayTooltipLastCheckedDay(date: String) {
        datastore.edit { preference ->
            preference[YESTERDAY_TOOLTIP_KEY] = date
        }
    }

    fun getStarBottleListTooltipState(): Flow<Boolean> = datastore.data.map { preference ->
        preference[STAR_BOTTLE_LIST_TOOLTIP] ?: true
    }

    suspend fun setStarBottleListTooltipState(state: Boolean) {
        datastore.edit { preference ->
            preference[STAR_BOTTLE_LIST_TOOLTIP] = state
        }
    }

    fun getStarBottleListBannerState(): Flow<Boolean> = datastore.data.map { preference ->
        preference[STAR_BOTTLE_LIST_BANNER] ?: true
    }

    suspend fun setStarBottleListBannerState(state: Boolean) {
        datastore.edit { preference ->
            preference[STAR_BOTTLE_LIST_BANNER] = state
        }
    }

    fun getStarBottleOpenSheetShownMonth(): Flow<Int> = datastore.data.map { preference ->
        preference[STAR_BOTTLE_OPEN_SHEET] ?: 0
    }

    suspend fun setStarBottleOpenSheetShownMonth(month: Int) {
        datastore.edit { preference ->
            preference[STAR_BOTTLE_OPEN_SHEET] = month
        }
    }

//    fun getBgmPlayStatus(): Flow<Boolean> = datastore.data.map { preference ->
//        preference[BGM_PLAY_STATUS] ?: false
//    }
//
//    suspend fun setBgmPlayStatus(state: Boolean) {
//        datastore.edit { preference ->
//            preference[BGM_PLAY_STATUS] = state
//        }
//    }
}