#!/bin/bash

VERSION=$(echo "cat //*[local-name()='project']/*[local-name()='version']" | xmllint --shell pom.xml | sed '/^\/ >/d' | sed 's/<[^>]*.//g')

curl https://github.com/aexoti/atlas/releases/download/atlas-$VERSION/atlas-$VERSION-bin.zip > /tmp/atlas.zip
cd /tmp/

unzip /tmp/atlas.zip

if [ $TYPE -eq 'master' ];
then
	java -cp /tmp/atlas.jar br.com.aexo.atlas.master.AtlasMaster 
fi

if [ $TYPE -eq 'slave' ];
then
	java -cp /tmp/atlas.jar br.com.aexo.atlas.slave.AtlasSlave 
fi


