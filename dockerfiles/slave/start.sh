#!/bin/bash

VERSION=$(echo "cat //*[local-name()='project']/*[local-name()='version']" | xmllint --shell pom.xml | sed '/^\/ >/d' | sed 's/<[^>]*.//g')

cd /atlas/

if [ -f /atlas/atlas.zip ]; then
   echo "version exists, continue..."
else
   curl https://github.com/aexoti/atlas/releases/download/atlas-$VERSION/atlas-$VERSION-bin.zip > /atlas/atlas.zip
   unzip /atlas/atlas.zip
fi

sh /atlas/atlas/slave.sh