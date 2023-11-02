/*
 * Copyright 2015 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.server

import com.example.grpc_app_demo.GreeterGrpc.GreeterImplBase
import com.example.grpc_app_demo.HelloRequest
import com.example.grpc_app_demo.HelloResponse
import io.grpc.Grpc
import io.grpc.InsecureServerCredentials
import io.grpc.Server
import io.grpc.ServerRegistry
import io.grpc.netty.NettyServerProvider
import io.grpc.stub.StreamObserver
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

/**
 * Server that manages startup/shutdown of a `Greeter` server.
 */
class HelloWorldServer {
    private var server: Server? = null

    @Throws(IOException::class)
    private fun start() {
        ServerRegistry.getDefaultRegistry().register(NettyServerProvider())
        /* The port on which the server should run */
        val port = 50051
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
            .addService(GreeterImpl())
            .build()
            .start()
        logger.info("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down")
                try {
                    this@HelloWorldServer.stop()
                } catch (e: InterruptedException) {
                    e.printStackTrace(System.err)
                }
                System.err.println("*** server shut down")
            }
        })
    }

    @Throws(InterruptedException::class)
    private fun stop() {
        if (server != null) {
            server!!.shutdown().awaitTermination(30, TimeUnit.SECONDS)
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    @Throws(InterruptedException::class)
    private fun blockUntilShutdown() {
        if (server != null) {
            server!!.awaitTermination()
        }
    }

    internal class GreeterImpl : GreeterImplBase() {
        override fun sayHello(req: HelloRequest, responseObserver: StreamObserver<HelloResponse>) {
            val reply = HelloResponse.newBuilder().setMessage("Hello " + req.name).build()
            responseObserver.onNext(reply)
            responseObserver.onCompleted()
        }
    }

    companion object {
        private val logger = Logger.getLogger(
            HelloWorldServer::class.java.name
        )

        /**
         * Main launches the server from the command line.
         */
        @Throws(IOException::class, InterruptedException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val server = HelloWorldServer()
            server.start()
            server.blockUntilShutdown()
        }
    }
}
