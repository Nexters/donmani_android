package com.gowoon.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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
        private val BBS_RULE_SHEET_KEY = booleanPreferencesKey("bbs_rule_sheet_key")
        private val ON_BOARDING_KEY = booleanPreferencesKey("onboarding_key")
    }

    fun getNoConsumptionTooltipState(): Flow<Boolean> = datastore.data.map { preference ->
        preference[NO_CONSUMPTION_TOOLTIP_KEY] ?: true
    }

    suspend fun setNoConsumptionTooltipState(state: Boolean) {
        datastore.edit { preference ->
            preference[NO_CONSUMPTION_TOOLTIP_KEY] = state
        }
    }

    fun getBBSRuleSheetState(): Flow<Boolean> = datastore.data.map { preference ->
        preference[BBS_RULE_SHEET_KEY] ?: true
    }

    suspend fun setBBSRuleSheetState(state: Boolean) {
        datastore.edit { preference ->
            preference[BBS_RULE_SHEET_KEY] = state
        }
    }

    suspend fun getOnBoardingState(): Boolean = datastore.data.first()[ON_BOARDING_KEY] ?: true

    suspend fun setOnBoardingState(state: Boolean) {
        datastore.edit { preference ->
            preference[ON_BOARDING_KEY] = state
        }
    }

}