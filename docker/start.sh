#!/bin/bash

touch /var/run/haproxy.pid
mkdir /run/haproxy

haproxy  -f /usr/local/etc/haproxy/haproxy.cfg -p /var/run/haproxy.pid & 

/atlas/wildfly-9.0.0.Final/bin/standalone.sh -b 0.0.0.0 -Djboss.http.port=30000 -DCALLBACK=$CALLBACK -DNAMESPACE=$NAMESPACE -DZK_SERVER=$ZK_SERVER -DMARATHON_URL=$MARATHON_URL -DCONFIG_FILE_NAME=$CONFIG_FILE_NAME -DCOMMAND=$COMMAND