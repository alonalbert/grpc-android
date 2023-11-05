package com.example.grpc.android.server

import com.example.grpc_app_demo.GreeterGrpcKt.GreeterCoroutineStub
import com.example.grpc_app_demo.HelloResponse
import com.example.grpc_app_demo.helloRequest
import io.grpc.ManagedChannelBuilder

internal class GrpcServiceKotlin(private val host: String, private val port: Int = 50051)  {
  
  suspend fun greet(name: String): Reply<HelloResponse> {
    return try {
      val channel = ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build()
      try {
        val stub = GreeterCoroutineStub(channel)
        val request = helloRequest { this.name = name }
        runCatching { Reply.Success(stub.sayHello(request)) }.getOrElse { Reply.Error(it) }
      } finally {
        channel.shutdown()
      }
    } catch (e: Throwable) {
      Reply.Error(e)
    }
  }
}
