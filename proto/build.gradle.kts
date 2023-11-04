buildscript {
  dependencies {
    classpath(libs.protobuf.gradle.plugin)
  }
}
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("java-library")
  alias(libs.plugins.protobuf)
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

protobuf {
  protoc {
    artifact = "com.google.protobuf:protoc:${libs.versions.protoc.asProvider().get()}"
  }
  plugins {
    create("grpc") {
      artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.get()}"
    }
    create("grpckt") {
      artifact = "io.grpc:protoc-gen-grpc-kotlin:${libs.versions.protoc.kotlin.get()}:jdk8@jar"
    }
  }
  generateProtoTasks {
    all().forEach {
      it.builtins {
        named("java") {
          option("lite")
        }
        create("kotlin") {
          option("lite")
        }
      }
      it.plugins {
        create("grpc") {
          option("lite")
        }
        create("grpckt") {
          option("lite")
        }
      }
    }
  }
}

dependencies {
  implementation(libs.grpc.protobuf.lite)
  implementation(libs.grpc.stub)
  implementation(libs.guava)
  implementation(libs.checker.qual)
  implementation(libs.tomcat.annotations)
}