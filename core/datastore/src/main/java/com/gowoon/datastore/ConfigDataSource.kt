package com.gowoon.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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
}