#!/bin/bash

INPUT=""                                               # Input cache
DOCKER_COMPOSE="docker-compose.yml"                    # Docker compose file
APP_CONFIG="config/application.yml"                    # App config file
RABBITMQ_CONFIG="assets/rabbitmq/rabbitmq.conf"        # RabbitMQ Config file
TRAEFIK_CONFIG="assets/traefik/config.yml"             # Traefik static config file
TRAEFIK_DYN_CONFIG="assets/traefik/dynamic-config.yml" # Traefik dynamic config

# $1 text
function print() {
  echo "[*] $1"
}

# $1 text
function error() {
  echo "[!] Error: $1"
}

# $1 prompt
function input() {
  read -p "[*] $1: " INPUT
  INPUT=${INPUT:-None}
}

# $1 prompt
# $2 default value
function input_default() {
  read -p "[*] $1 [$2]: " INPUT
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

function reset() {
  print "About to reset your current configuration. Press Ctl+C to cancel"
  print "[ENTER]"
  read

  print "Resetting config ..."

  git checkout -- $APP_CONFIG &>/dev/null
  git checkout -- $DOCKER_COMPOSE &>/dev/null
  git checkout -- $RABBITMQ_CONFIG &>/dev/null
  git checkout -- $TRAEFIK_CONFIG &>/dev/null
  git checkout -- $TRAEFIK_DYN_CONFIG &>/dev/null
  git checkout -- $TRAEFIK_USERS &>/dev/null

  print
}

# $1 file
# $2 find
# $3 replace
function replace() {
  sed -i "s/$2/$3/g" "$1"
}

# $1 file
# $2 key
# $3 value
function write() {
  yq w -i "$1" "$2" "$3"
}

# $1 file
# $2 key
function delete() {
  yq d -i "$1" "$2"
}

# $1 user
# $2 password
# $3 root password
function save_db_config() {
  write $DOCKER_COMPOSE "services.database.environment.MYSQL_USER" "$1"
  write $APP_CONFIG "spring.datasource.username" "$1"

  write $DOCKER_COMPOSE "services.database.environment.MYSQL_PASSWORD" "$2"
  write $APP_CONFIG "spring.datasource.password" "$2"

  write $DOCKER_COMPOSE "services.database.environment.MYSQL_ROOT_PASSWORD" "$3"
}

# $1 username
# $2 password
# $3 broker port
function save_rabbitmq_config() {
  replace $RABBITMQ_CONFIG "default_user = olivia" "default_user = $1"
  replace $RABBITMQ_CONFIG "default_pass = olivia" "default_pass = $2"
  replace $RABBITMQ_CONFIG "listeners.tcp.default = 5672" "listeners.tcp.default = $3"

  write $APP_CONFIG "spring.rabbitmq.username" "$1"
  write $APP_CONFIG "spring.rabbitmq.password" "$2"
  write $APP_CONFIG "spring.rabbitmq.port" "$3"
}

# $1 domain
# $2 email
function save_traefik_config() {
  if [ "$1" == "None" ] || [ "$2" == "None" ]; then
    delete $TRAEFIK_CONFIG "certificatesResolvers"
    write $TRAEFIK_DYN_CONFIG "http.routers.backend-router.tls" "{}"
    write $TRAEFIK_DYN_CONFIG "http.routers.proxy-router.tls" "{}"
    write $TRAEFIK_DYN_CONFIG "http.routers.mq-dashboard-router.tls" "{}"
    replace $TRAEFIK_DYN_CONFIG "'{}'" "{}"
  else
    replace $TRAEFIK_CONFIG "<email-address>" "$2"
    replace $TRAEFIK_DYN_CONFIG "<domain>" "$1"
  fi
}

# $1 username
# $2 password
function save_init_admin_user() {
  PASSWORD=$(echo -n "$2" | sha256sum)
  write $APP_CONFIG "admin.initial-username" "$1"
  write $APP_CONFIG "admin.initial-password" "$PASSWORD"
}

function initial_config() {
  print "Initial Config Requieres a config reset"
  reset

  print "Starting Initial Config ..."

  print
  print "Database"

  input_default "Enter a database user" "olivia"
  DB_USER=$INPUT
  password_generate "Enter a database password"
  DB_PASSWORD=$INPUT
  ROOT_PASSWORD=$(pwgen 16 -a)
  print "Generated root password: $ROOT_PASSWORD"
  save_db_config "$DB_USER" "$DB_PASSWORD" "$ROOT_PASSWORD"

  print
  print "RabbitMQ"

  input_default "Enter a rabbitmq username" "olivia"
  RMQ_USER=$INPUT
  password_generate "Enter a rabbitmq password"
  RMQ_PASSWORD=$INPUT
  input_default "Enter a broker tcp port" "5672"
  RMQ_PORT=$INPUT
  save_rabbitmq_config "$RMQ_USER" "$RMQ_PASSWORD" "$RMQ_PORT"

  print
  print "Traefik"
  input_default "Enter the domain used for this server" "None"
  DOMAIN=$INPUT

  if ! [ "$DOMAIN" == "None" ]; then
    EMAIL="None"

    while [ "$EMAIL" == "None" ]; do
      input "Enter a valid email address to register the Let's Encrypt SSL Certificates"
      EMAIL=$INPUT
    done
    save_traefik_config "$DOMAIN" "$EMAIL"
  else
    print "Using no Domain. Traefik will generate a self signed certificate"
    save_traefik_config "None" "None"
  fi

  print
  input_default "Enter a admin user" "admin"
  ADMIN_USER=$INPUT
  password_default "Enter a password for the admin user" "admin"
  ADMIN_PASSWORD=$INPUT
  save_init_admin_user "$ADMIN_USER" "$ADMIN_PASSWORD"

  print
  print "Finished Initial Config"
}

function check_deps() {
  if ! [ "$(command -v yq)" ]; then
    error "yq not installed"
    print "See https://mikefarah.github.io/yq/" >&2
    exit 0
  fi
  if ! [ "$(command -v git)" ]; then
    error "git not installed"
    exit 0
  fi
  if ! [ "$(command -v pwgen)" ]; then
    error "pwgen not installed"
    exit 0
  fi
  if ! [ "$(command -v htpasswd)" ]; then
    error "htpasswd not installed"
    exit 0
  fi
  if ! [ "$(command -v sha256sum)" ]; then
    error "sha256sum not installed"
    exit 0
  fi
}

print "Olivia Backend Configuration Script"
print

check_deps

if [ "$1" == "init" ]; then
  initial_config
elif [ "$1" == "reset" ]; then
  reset
else
  print "Use one of the following options: init, reset"
fi
