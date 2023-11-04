@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("java-library")
  alias(libs.plugins.kotlin)
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
  implementation(project(":proto"))
  implementation(libs.grpc.core)
  implementation(libs.grpc.protobuf.lite)
  implementation(libs.grpc.netty)
  implementation(libs.grpc.stub)
  implementation(libs.grpc.services)
}