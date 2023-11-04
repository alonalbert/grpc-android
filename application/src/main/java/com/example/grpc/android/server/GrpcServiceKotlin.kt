package com.example.grpc.android.server

import com.example.grpc_app_demo.GreeterGrpcKt.GreeterCoroutineStub
import com.example.grpc_app_demo.HelloResponse
import com.example.grpc_app_demo.helloRequest
import io.grpc.ManagedChannelBuilder
import java.util.concurrent.TimeUnit

class GrpcServiceKotlin(host: String, port: Int = 50051): AutoCloseable {
  private val channel = ManagedChannelBuilder.forAddress(host, port)
    .usePlaintext()
    .build()

  private val stub: GreeterCoroutineStub = GreeterCoroutineStub(channel)

  suspend fun greet(name: String): HelloResponse {
    val request = helloRequest { this.name = name }
    return stub.sayHello(request)
  }

  override fun close() {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }
}
