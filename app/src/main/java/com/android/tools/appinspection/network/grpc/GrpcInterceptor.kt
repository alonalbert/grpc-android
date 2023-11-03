package com.android.tools.appinspection.network.grpc

import android.util.Log
import io.grpc.Attributes
import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ClientStreamTracer
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener
import io.grpc.Grpc.TRANSPORT_ATTR_REMOTE_ADDR
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.Status

internal class GrpcInterceptor : ClientInterceptor {
  override fun <Req, Res> interceptCall(
    method: MethodDescriptor<Req, Res>,
    options: CallOptions,
    next: Channel
  ): ClientCall<Req, Res> =
    InterceptingClientCall(method, next.newCall(method, options.withStreamTracerFactory(StreamTracer.Factory())))

  private class InterceptingClientCall<Req, Res>(private val method: MethodDescriptor<Req, Res>, delegate: ClientCall<Req, Res>) : SimpleForwardingClientCall<Req, Res>(delegate) {
    override fun start(responseListener: Listener<Res>, headers: Metadata) {
      val listener = ClientCallListener(responseListener)
      Log.d("Inspector", "Request started: method=${method.fullMethodName} headers=$headers")
      super.start(listener, headers)
    }

    override fun sendMessage(message: Req) {
      super.sendMessage(message)
      Log.d("Inspector", "Request payload: ${message.toString().substringAfter('\n')}")
    }
  }

  private class StreamTracer : ClientStreamTracer() {
    override fun streamCreated(transportAttrs: Attributes, headers: Metadata) {
      val address = transportAttrs.get(TRANSPORT_ATTR_REMOTE_ADDR)
      Log.d("Inspector", "streamCreated: address: $address headers=$headers")
    }

    override fun streamClosed(status: Status) {
      Log.d("Inspector", "streamClosed: $status")
    }

    override fun inboundTrailers(trailers: Metadata) {
      Log.d("Inspector", "Trailers: $trailers")
    }

    class Factory : ClientStreamTracer.Factory() {
      override fun newClientStreamTracer(info: StreamInfo, headers: Metadata) = StreamTracer()
    }
  }
}

class ClientCallListener<Res>(responseListener: ClientCall.Listener<Res>) :
  SimpleForwardingClientCallListener<Res>(responseListener) {
  override fun onMessage(message: Res) {
    super.onMessage(message)
    Log.d("Inspector", "Response payload: ${message.toString().substringAfter('\n')}")
  }

  override fun onClose(status: Status?, trailers: Metadata?) {
    super.onClose(status, trailers)
    Log.d("Inspector", "onClose: $status")
  }
}
