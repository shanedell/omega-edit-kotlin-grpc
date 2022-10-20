package com.sdell.omega_edit.grpc

import io.grpc.Server
import io.grpc.ServerBuilder

class OmegaEditServer(private val port: Int) {
    private val editorService = EditorService()
    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(editorService)
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@OmegaEditServer.stop()
                println("*** server shut down")
            }
        )
    }

    private fun stop() {
        editorService.closeChannels()
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 9000
    val server = OmegaEditServer(port)
    server.start()
    server.blockUntilShutdown()
}
