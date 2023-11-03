package com.example.server;

import com.example.grpc_app_demo.GreeterGrpc;
import com.example.grpc_app_demo.HelloRequest;
import com.example.grpc_app_demo.HelloResponse;

import io.grpc.stub.StreamObserver;

public class GreeterService extends GreeterGrpc.GreeterImplBase {
  @Override
  public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
    final HelloResponse response = HelloResponse.newBuilder().setMessage("Hello " + request.getName()).build();
    System.out.printf("Request: %s Response: %s\n", request, response);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
