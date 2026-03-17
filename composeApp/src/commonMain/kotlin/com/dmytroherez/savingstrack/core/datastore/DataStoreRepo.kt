package com.dmytroherez.savingstrack.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.dmytroherez.savingstrack.core.datastore.DataStoreRepo.Keys.IS_LOGGED_IN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepo(private val dataStore: DataStore<Preferences>) {
    private object Keys {
        val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    suspend fun setIsLoggedIn() {
        dataStore.edit {
            it[IS_LOGGED_IN] = true
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it[IS_LOGGED_IN] = false
        }
    }

}