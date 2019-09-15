#!/bin/bash

echo "[*] Building JAR..."
mvn clean package

echo "[*] Building Container"

if [ $EUID -eq 0 ]; then
  docker build -t olivia-backend .
else
  sudo docker build -t olivia-backend .
fi