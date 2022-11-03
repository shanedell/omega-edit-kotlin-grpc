
rootProject.name = "omega-edit-kotlin-grpc"

include("protos", "stub", "server")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}
