#!/bin/bash

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

if [ -z "${CALLBACK}" ]; then
      echo "env var CONF_BALANCER_DEST is not defined"
    exit 1;
fi

java -cp atlas.jar br.com.aexo.atlas.master.AtlasMaster