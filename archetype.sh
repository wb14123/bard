#!/bin/sh

VERSION="0.0.1-SNAPSHOT"
MAVEN_GOAL=$1

mvn clean

mvn $MAVEN_GOAL || exit 1
cd bard-archetype
mvn archetype:create-from-project -Darchetype.keepParent=false
cd target/generated-sources/archetype
sed -i "" \
"s/<artifactId>bard-simple-archetype<\/artifactId>/\
<parent>\
    <groupId>com.bardframework<\/groupId>\n\
    <artifactId>bard<\/artifactId>\n\
    <version>$VERSION<\/version>\n\
<\/parent>\n/g" pom.xml
mvn $MAVEN_GOAL
