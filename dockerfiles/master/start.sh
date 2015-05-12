#!/bin/bash
cd /atlas/

VERSION=$(echo "cat //*[local-name()='project']/*[local-name()='version']" | xmllint --shell pom.xml | sed '/^\/ >/d' | sed 's/<[^>]*.//g')

echo $VERSION

if [ -f /atlas/atlas.zip ]; then
   echo "version exists, continue..."
else
   curl -sSL https://github.com/aexoti/atlas/releases/download/atlas-$VERSION/atlas-bin.zip > /atlas/atlas.zip
   unzip /atlas/atlas.zip
fi
cd /atlas/atlas
sh /atlas/atlas/master.sh