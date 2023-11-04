package com.example.grpc.android.server

import com.example.grpc_app_demo.GreeterGrpc
import com.example.grpc_app_demo.HelloRequest
import com.example.grpc_app_demo.HelloResponse
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GrpcService(host: String, port: Int = 50051) {
  private val channel = ManagedChannelBuilder.forAddress(host, port)
    .usePlaintext()
    .build()

  private val blockingStub = GreeterGrpc.newBlockingStub(channel)
  private val stub = GreeterGrpc.newStub(channel)

  fun sendBlockingHello(name: String): HelloResponse =
    blockingStub.sayHello(HelloRequest.newBuilder().setName(name).build())

  suspend fun sendHello(name: String): HelloResponse = withContext(Dispatchers.IO) {
    val request = HelloRequest.newBuilder().setName(name).build()
    suspendCoroutine {
      stub.sayHello(request, object : StreamObserver<HelloResponse> {
        override fun onNext(value: HelloResponse) {
          it.resumeWith(Result.success(value))
        }

        override fun onError(t: Throwable) {
          it.resumeWithException(t)
        }

        override fun onCompleted() {
        }
      })
    }
  }
}
