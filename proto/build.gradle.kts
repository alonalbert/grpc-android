import com.google.protobuf.gradle.builtins
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
  id("java-library")
  id("com.google.protobuf")
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

protobuf {
  protoc {
    artifact = "com.google.protobuf:protoc:3.19.2"
  }
  plugins {
    create("grpc") {
      artifact = "io.grpc:protoc-gen-grpc-java:1.59.0"
    }
    create("grpckt") {
      artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.0:jdk8@jar"
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
  implementation("io.grpc:grpc-protobuf-lite:1.59.0")
  implementation("io.grpc:grpc-stub:1.59.0")
  implementation("com.google.guava:guava:21.0")
  implementation("org.checkerframework:checker-qual:3.34.0")
  implementation("org.apache.tomcat:annotations-api:6.0.53")
}