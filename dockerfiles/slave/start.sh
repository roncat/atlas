#!/bin/bash

cd /atlas/atlas

touch /var/run/haproxy.pid
haproxy  -f /usr/local/etc/haproxy/haproxy.cfg -p /var/run/haproxy.pid & 

java -cp atlas.jar br.com.aexo.atlas.slave.AtlasSlave