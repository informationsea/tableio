#!/bin/sh
./gradlew clean
./gradlew build generatePomFileForMavenJavaPublication pP publishToMavenLocal
