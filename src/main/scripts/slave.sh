#!/bin/bash

./atlas.conf

if [ -z "${ZK}" ]; then
      echo "env var ZK is not defined"
    exit 1;
fi

if [ -z "${NAMESPACE}" ]; then
      echo "env var NAMESPACE is not defined"
    exit 1;
fi

if [ -z "${MARATHON_URL}" ]; then
     echo "env var MARATHON_URL is not defined"
    exit 1;
fi

if [ -z "${HOSTNAME}" ]; then
      echo "env var HOSTNAME is not defined"
    exit 1;
fi

if [ -z "${PORT}" ]; then
      echo "env var PORT is not defined"
    exit 1;
fi

if [ -z "${CONF_BALANCER_DEST}" ]; then
      echo "env var CONF_BALANCER_DEST is not defined"
    exit 1;
fi

if [ -z "${COMMAND}" ]; then
    echo "env var COMMAND is not defined"
    exit 1;
fi

java -cp atlas.jar br.com.aexo.atlas.slave.AtlasSlave