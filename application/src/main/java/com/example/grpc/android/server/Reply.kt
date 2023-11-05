package com.example.grpc.android.server

internal sealed class Reply<T> {
  class Success<T>(val value: T) : Reply<T>()
  class Error<T>(val t: Throwable) : Reply<T>()
}
