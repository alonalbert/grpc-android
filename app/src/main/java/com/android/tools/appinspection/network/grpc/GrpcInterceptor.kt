package com.android.tools.appinspection.network.grpc

import io.grpc.Attributes
import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ClientStreamTracer
import io.grpc.ClientStreamTracer.StreamInfo
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.Status

internal class GrpcInterceptor : ClientInterceptor {
  override fun <Req, Res> interceptCall(
    method: MethodDescriptor<Req, Res>,
    callOptions: CallOptions,
    next: Channel
  ): ClientCall<Req, Res> {
    val options = callOptions.withStreamTracerFactory(object : ClientStreamTracer.Factory() {
      override fun newClientStreamTracer(info: StreamInfo, headers: Metadata): ClientStreamTracer {
        return StreamTracer(method)
      }
    })
    return InterceptingClientCall(next.newCall(method, options))
  }

  private class InterceptingClientCall<Req, Res>(delegate: ClientCall<Req, Res>) : SimpleForwardingClientCall<Req, Res>(delegate) {
    override fun start(responseListener: Listener<Res>, headers: Metadata) {
      val listener = object : SimpleForwardingClientCallListener<Res>(responseListener) {
        override fun onMessage(message: Res) {
          super.onMessage(message)
          println("onMessage: $message")
        }
      }
      super.start(listener, headers)
      println("start")
    }

    override fun sendMessage(message: Req) {
      super.sendMessage(message)
      println("sendMessage: $message")
    }
  }

  private class StreamTracer<Req, Res>(private val method: MethodDescriptor<Req, Res>) : ClientStreamTracer() {
    override fun streamCreated(transportAttrs: Attributes, headers: Metadata) {
      println("streamCreated: $method")
    }

    override fun streamClosed(status: Status) {
      println("streamClosed: $status")
    }
  }
}