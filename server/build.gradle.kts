plugins {
  id("java-library")
  id("org.jetbrains.kotlin.jvm")
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
  implementation("io.grpc:grpc-core:1.47.0")
  implementation("io.grpc:grpc-protobuf-lite:1.47.0")
  implementation("io.grpc:grpc-netty:1.47.0")
  implementation("io.grpc:grpc-stub:1.47.0")
  implementation("io.grpc:grpc-services:1.47.0")
  implementation(project(":proto"))
}