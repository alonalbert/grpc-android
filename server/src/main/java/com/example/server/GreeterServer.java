package com.example.server;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerRegistry;
import io.grpc.netty.NettyServerProvider;
import io.grpc.protobuf.services.ProtoReflectionService;

public class GreeterServer {
  private final int port;

  private final Server server;

  public GreeterServer(int port) {
    this.port = port;
    ServerRegistry.getDefaultRegistry().register(new NettyServerProvider());
    server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
        .addService(ProtoReflectionService.newInstance())
        .addService(new GreeterService())
        .build();
  }

  public void start() throws IOException {
    server.start();
    System.out.println("Server started, listening on " + port);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.err.println("*** shutting down gRPC server since JVM is shutting down");
      try {
        GreeterServer.this.stop();
      } catch (InterruptedException e) {
        e.printStackTrace(System.err);
      }
      System.err.println("*** server shut down");
    }));
  }

  public void stop() throws InterruptedException {
    server.awaitTermination(5, TimeUnit.SECONDS);
  }

  public void awaitTermination() throws InterruptedException {
    server.awaitTermination();
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    GreeterServer server = new GreeterServer(50051);
    server.start();
    server.awaitTermination();
  }
}
