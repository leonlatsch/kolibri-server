#!/bin/bash

echo "[*] Building JAR..."
mvn clean package

echo "[*] Building Container"
sudo docker build -t leonlatsch/olivia-backend .
