#!/bin/sh
mvn dependency:copy-dependencies
mvn install
java -cp "target/Lab1-0.0.1-SNAPSHOT.jar:target/dependency/*" com.diffusion.training.lab1.HelloWorldPublisher

