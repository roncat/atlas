#!/bin/bash

curl https://github.com/aexoti/atlas/releases/download/atlas-$VERSION/atlas-$VERSION-jar-with-dependencies.jar > /tmp/atlas.jar

if [ $TYPE -eq 'master' ] then
	java -cp /tmp/atlas.jar br.com.aexo.atlas.master.AtlasMaster 
fi

if [ $TYPE -eq 'slave' ] then
	java -cp /tmp/atlas.jar br.com.aexo.atlas.slave.AtlasSlave 
fi


