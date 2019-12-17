#!/bin/bash

INPUT="" # Input cache
DOCKER_COMPOSE="docker-compose.yml" # Docker compose file
APP_CONFIG="config/application.yml" # App config file


# $1 text
function print() {
  echo "[*] $1"
}

# $1 text
function error() {
    echo "[!] $1"
}

# $1 prompt
function input() {
    read -p "[*] $1: " INPUT
}

# $1 prompt
# $2 default value
function input_default() {
    read -p "[*] $1 [$2]:" INPUT
    INPUT=${INPUT:-$2}
}

# $1 pormpt
function password() {
    read -s -p "[*] $1: " INPUT
    echo
}

# $1 prompt
# $2 default
function password_default() {
    read -s -p "[*] $1 [$2]: " INPUT
    INPUT=${INPUT:-$2}
    echo
}

# $1 prompt
function password_generate() {
    read -s -p "[*] $1 [Generated]: " INPUT
    INPUT=${INPUT:-$(pwgen 16 -1)}
    echo
}

# $1 search
# $2 replace
# $3 file
function persist() {
    sed -i "s/$1/$2/g" $3
}

# $1 file
# $2 key
# $3 value
function write() {
    yq w -i $1 $2 $3
}

# $1 user
# $2 password
function save_db_config() {
    write $DOCKER_COMPOSE "services.database.environment.MYSQL_USER" $1
    write $APP_CONFIG "spring.datasource.username" $1

    write $DOCKER_COMPOSE "services.database.environment.MYSQL_PASSWORD" $2
    write $APP_CONFIG "spring.datasource.password" $2
}

function initial_config() {
    print "Starting Initial Config ..."
    print

    input_default "Enter a database user" "olivia"
    DB_USER=$INPUT

    password_generate "Enter a database password"
    DB_PASSWORD=$INPUT

    save_db_config $DB_USER $DB_PASSWORD
}

print "Olivia Backend Configuration Script"
print

initial_config
