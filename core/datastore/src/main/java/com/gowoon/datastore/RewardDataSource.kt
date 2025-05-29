package com.gowoon.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.gowoon.datastore.di.RewardDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RewardDataSource @Inject constructor(
    @RewardDataStore private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val SHOW_REWARD_RECEIVED_TOOLTIP =
            booleanPreferencesKey("show_reward_received_tooltip_key")
    }

    fun getShowRewardReceivedTooltip(): Flow<Boolean> = dataStore.data.map { preference ->
        preference[SHOW_REWARD_RECEIVED_TOOLTIP] ?: false
    }

    suspend fun setShowRewardReceivedTooltip(state: Boolean) {
        dataStore.edit { preference ->
            preference[SHOW_REWARD_RECEIVED_TOOLTIP] = state
        }
    }
}