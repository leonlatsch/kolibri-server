#!/bin/bash

# $1 tag
function build_image() {
    echo "[*] Building Image ..."
    sudo docker build -t leonlatsch/olivia-backend:$1 .
}

function build_jar() {
    echo "[*] Building JAR ..."
    mvn clean package
}

build_jar

if [ $# -gt 0 ]; then
    build_image "$1"
else
    build_image "latest"
fi