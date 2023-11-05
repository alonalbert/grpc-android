package com.example.grpc.android

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.grpc.android.server.GrpcServiceKotlin
import com.example.grpc.android.ui.theme.GrpcAndroidTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private suspend fun <T> Context.getSetting(key: Preferences.Key<T>, defaultValue: T): T = dataStore.data.first()[key]?: defaultValue
private suspend fun <T> Context.putSetting(key: Preferences.Key<T>, value: T) {
  dataStore.updateData {
    it.toMutablePreferences().apply {
      putAll(key to value)
    }
  }
}

private val HOST = stringPreferencesKey("host")

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      GrpcAndroidTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          App()
        }
      }
    }
  }

  @Composable
  fun App() {
    val scope = rememberCoroutineScope()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      var response by remember { mutableStateOf("") }
      var host by remember { mutableStateOf("") }
      LaunchedEffect(Unit) {
        host = getSetting(HOST, "10.0.0.191")
      }

      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {

        Text("Host:")
        TextField(value = host, onValueChange = {
          host = it
          scope.launch {
            putSetting(HOST, host)
          }
        })
      }

      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        var message by remember { mutableStateOf("") }


        Button(onClick = {
          scope.launch {
            GrpcServiceKotlin(host).use {
              response = it.greet(message).message
            }
          }
        }) {
          Text("Send request")
        }
        TextField(value = message, onValueChange = { message = it })
      }
      Text(text = response,
        Modifier
          .fillMaxSize()
          .border(1.dp, Color.Gray))
    }
  }

  @Preview(showBackground = true)
  @Composable
  fun GreetingPreview() {
    GrpcAndroidTheme {
      App()
    }
  }
}
