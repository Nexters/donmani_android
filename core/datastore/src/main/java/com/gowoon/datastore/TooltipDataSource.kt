package com.gowoon.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TooltipDataSource @Inject constructor(
    private val datastore: DataStore<Preferences>
) {
    companion object {
        private val NO_CONSUMPTION_TOOLTIP_KEY = booleanPreferencesKey("no_consumption_tooltip_key")
    }

    fun getNoConsumptionTooltipState(): Flow<Boolean> = datastore.data.map { preference ->
        preference[NO_CONSUMPTION_TOOLTIP_KEY] ?: true
    }

    suspend fun setNoConsumptionTooltipState(state: Boolean) {
        datastore.edit { preference ->
            preference[NO_CONSUMPTION_TOOLTIP_KEY] = state
        }
    }
}