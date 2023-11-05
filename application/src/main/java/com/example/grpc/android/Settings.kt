package com.example.grpc.android

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
private val HOST = stringPreferencesKey("host")

internal class Settings(private val context: Context) {

  val getHost: Flow<String> = context.dataStore.data.map { preferences ->
    preferences[HOST] ?: ""
  }

  suspend fun saveHost(token: String) {
    context.dataStore.edit { preferences ->
      preferences[HOST] = token
    }
  }
}
