#!/bin/sh
./gradlew clean build generatePomFileForMavenJavaPublication pP publishToMavenLocal
