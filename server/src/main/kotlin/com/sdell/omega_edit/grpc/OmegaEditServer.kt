package com.sdell.omega_edit.grpc

import io.grpc.Server
import io.grpc.ServerBuilder

class OmegaEditServer(private val port: Int) {
    private val editorService = EditorService()
    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(editorService)
        .build()

    // start server
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

    // stop server
    private fun stop() {
        editorService.closeChannels()
        server.shutdown()
    }

    // keep server running till terminated
    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}

fun main(args: Array<String>) {
    var port = 9000 // default port

    // check to see if user specified different port using arg or env variable
    if (args.isNotEmpty()) {
        args.forEach { arg ->
            run {
                if (arg.contains("--target="))
                    port = arg.split("=")[1].toInt()
            }
        }
    } else {
        port = System.getenv("PORT")?.toInt() ?: port
    }

    val server = OmegaEditServer(port)
    server.start()
    server.blockUntilShutdown()
}
