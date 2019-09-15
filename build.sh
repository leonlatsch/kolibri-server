#!/bin/bash

echo "[*] Building JAR..."
mvn clean package

echo "[*] Building Container"
docker build -t olivia-backend .
