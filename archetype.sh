#!/bin/sh

VERSION=`cat version.txt`
MAVEN_GOAL=$1

mvn clean

mvn $MAVEN_GOAL || exit 1
cd bard-archetype
mvn archetype:create-from-project -Darchetype.keepParent=false
cd target/generated-sources/archetype

SED_CMD="s/<groupId>com.bardframework<\/groupId>/<parent><groupId>com.bardframework<\/groupId><artifactId>bard<\/artifactId><version>$VERSION<\/version><\/parent>/g"

if [ `uname` = 'Linux' ]; then
	sed -i "$SED_CMD" pom.xml
else
	sed -i '' "$SED_CMD" pom.xml
fi

mvn $MAVEN_GOAL
