# Olivia Backend System

![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/leonlatsch/olivia-backend?include_prereleases&label=version)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/leonlatsch/olivia-backend/Java%20CI)
![GitHub](https://img.shields.io/github/license/leonlatsch/olivia-backend)
![Maintenance](https://img.shields.io/maintenance/yes/2020)



## About

This repository provides a full server system used by the [Olivia app](https://github.com/leonlatsch/olivia).

It consists of the following components all tied up with docker-compose:

- [Olivia REST API](https://hub.docker.com/r/leonlatsch/olivia-backend)
- [A RabbitMQ Server](https://hub.docker.com/_/rabbitmq)
- [A MySQL Database](https://hub.docker.com/_/mysql)
- [A Traefik Reverse Proxy](https://hub.docker.com/_/traefik)



## Features

- It's easy to configure with a automatic configuration script.
- It requests and manages it's own SSL Certificates for all sites with [Let's Encrypt](https://letsencrypt.org/) due to Traefik.
- It relies on established technologies like spring, maven, amqp, mysql or docker.
- It does only save the information that is really needed. (No messages or chats).



## Getting Started

You can find all information about getting started in the [Wiki](https://github.com/leonlatsch/olivia-backend/wiki).



License
=======

    MIT License
    
    Copyright (c) 2019-2020 Leon Latsch
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

See `THIRD_PARTY` for third party licenses.
