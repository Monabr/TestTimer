package com.example.testtimer.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "app_prefs_store"

private val Context.dataStore by preferencesDataStore(
    name = PREFERENCES_NAME,
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, PREFERENCES_NAME))
    }
)

class PrefsStore @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore: DataStore<Preferences> = context.dataStore


    suspend fun setCurrentTime(currentTime: Long) {
        dataStore.edit {
            it[PreferencesKeys.CURRENT_TIME_KEY] = currentTime
        }
    }

    suspend fun getCurrentTime(): Flow<Long> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[PreferencesKeys.CURRENT_TIME_KEY] ?: 0L
    }

    suspend fun setIsTimerStarted(isStarted: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.IS_TIMER_STARTED_KEY] = isStarted
        }
    }

    suspend fun getIsTimerStarted(): Flow<Boolean> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[PreferencesKeys.IS_TIMER_STARTED_KEY] ?: false
    }


    private object PreferencesKeys {
        val CURRENT_TIME_KEY = longPreferencesKey("current_time")
        val IS_TIMER_STARTED_KEY = booleanPreferencesKey("is_timer_started")
    }
}