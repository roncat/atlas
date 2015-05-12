#!/bin/sh
 
origem=$(git rev-parse --abbrev-ref HEAD)
 
if [ $origem == "master" ] 
then
    # processo executado para suportar release branch, executando o fluxo gerando a partir do trunk
  
    #obtem versao do pom.xml do trunk
    VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | egrep -v '^\[|Downloading:' | tr -d ' \n')
    MAJOR=`echo $VERSION | awk -F '[/.]' '{print $1}'`
    MINOR=`echo $VERSION | awk -F '[/.]' '{print $2}'`
    BUGFIX='0-SNAPSHOT'
    
    # incrementa a minor version 
    NEW_MINOR=$((MINOR+1))

    GROUP_ID=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.groupId | egrep -v '^\[|Downloading:' | tr -d ' \n')
    ARTIFACT_ID=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.artifactId | egrep -v '^\[|Downloading:' | tr -d ' \n')
    
    
    mvn release:branch  -DbranchName=$MAJOR.$MINOR -Dproject.rel.$GROUP_ID:$ARTIFACT_ID=$MAJOR.$MINOR.$BUGFIX  -Dproject.dev.$GROUP_ID:$ARTIFACT_ID=$MAJOR.$NEW_MINOR.$BUGFIX

	if [ $? -ne 0 ]; then
	    exit 2; 
	fi
	 
    git checkout $MAJOR.$MINOR
else
    # processo executado para suportar release branch, executando o fluxo gerando a partir da release 


     git checkout $origem
fi


mvn -B release:prepare release:perform -Dmaven.test.skip=true
