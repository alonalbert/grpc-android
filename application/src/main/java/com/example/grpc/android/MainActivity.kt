package com.example.grpc.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.coroutineScope
import com.example.grpc.android.server.GrpcServiceKotlin
import com.example.grpc.android.ui.theme.GrpcAndroidTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      GrpcAndroidTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          App()
        }
      }
    }
  }

  @Composable
  fun App() {
    Column {
      var response by remember { mutableStateOf("") }

      Button(onClick = {
        sendRequest { response = it }
      }) {
        Text("Send request")
      }
      Text(text = response)
    }
  }

  private fun sendRequest(callback: (String) -> Unit) {
    lifecycle.coroutineScope.launch {
      GrpcServiceKotlin("10.0.0.191").use {
        callback(it.greet("FooBar").message)
      }
//      val response = GrpcService("10.0.0.191").sendHello("Foo")
//      withContext(Dispatchers.Main) {
//        callback(response.message)
//      }
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
