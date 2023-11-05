package com.example.grpc.android

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
import androidx.compose.runtime.collectAsState
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
import com.example.grpc.android.server.GrpcServiceKotlin
import com.example.grpc.android.ui.theme.GrpcAndroidTheme
import kotlinx.coroutines.launch

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
    val settings = Settings(this)
    val scope = rememberCoroutineScope()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      var response by remember { mutableStateOf("") }
      val host by settings.getHost.collectAsState(initial = "")
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {

        Text("Host:")
        TextField(value = host, onValueChange = {
          scope.launch {
            settings.saveHost(it)
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
      Text(
        text = response,
        Modifier
          .fillMaxSize()
          .border(1.dp, Color.Gray)
      )
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
