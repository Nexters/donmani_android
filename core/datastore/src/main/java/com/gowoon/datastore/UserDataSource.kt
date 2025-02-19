package com.gowoon.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.gowoon.datastore.di.UserDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataSource @Inject constructor(
    @UserDataStore private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val REGISTER_USER_STATE_KEY = booleanPreferencesKey("register_user_state_key")
        private val USER_NICKNAME = stringPreferencesKey("user_nickname")
    }

    fun getRegisterUserState(): Flow<Boolean> = dataStore.data.map { preference ->
        preference[REGISTER_USER_STATE_KEY] ?: false
    }

    suspend fun setRegisterUserState(state: Boolean) {
        dataStore.edit { preference ->
            preference[REGISTER_USER_STATE_KEY] = state
        }
    }

    fun getUserNickname(): Flow<String?> = dataStore.data.map { preference ->
        preference[USER_NICKNAME]
    }

    suspend fun setUserNickname(nickname: String) {
        dataStore.edit { preference ->
            preference[USER_NICKNAME] = nickname
        }
    }
}