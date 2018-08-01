# Error Management
[![Build Status](https://travis-ci.org/Inveasy/akka-serialization-protostuff.svg?branch=master)](https://travis-ci.org/Inveasy/akka-serialization-protostuff)
[![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=io.inveasy%3Aakka-serialization-protostuff&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.inveasy%3Aakka-serialization-protostuff)
[![Download](https://api.bintray.com/packages/inveasy/maven/akka-serialization-protostuff/images/download.svg) ](https://bintray.com/inveasy/maven/akka-serialization-protostuff/_latestVersion)

## What is it ?
This project provides an Akka serialization implementation with Protostuff.

## How To
First, include the maven dependency in your build :

```xml
<dependency>
  <groupId>io.inveasy</groupId>
  <artifactId>akka-serialization-protostuff</artifactId>
  <version>1.0.0</version>
</dependency>

<repositories>
  <repository>
    <id>bintray-inveasy-maven</id>
    <name>inveasy-maven</name>
    <url>https://dl.bintray.com/inveasy/maven</url>
  </repository>
</repositories>
```

Add it to your application.conf, or use Inveasy Configuration (a pre-reference.conf is provided) :
```hocon
akka {
  actor {
    serializers {
      protostuff = "io.inveasy.cluster.serialization.ProtostuffSerializer"
    }
    serialization-bindings {
      "io.inveasy.cluster.serialization.ProtostuffSerializable" = protostuff
    }
  }
}
```

All messages you want to be serialized with this serializer MUST implement interface ```ProtostuffSerializable```.

## Where is it used in Inveasy platform ?
Well, everywhere. It is the standard serializer for every messages which are not Akka.