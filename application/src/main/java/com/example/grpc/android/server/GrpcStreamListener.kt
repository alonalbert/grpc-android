package com.example.grpc.android.server

interface GrpcStreamListener<T> {
  fun onNext(response: T)
  fun onError(t: Throwable)
  fun onCompleted()
}