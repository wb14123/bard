#!/bin/sh

VERSION=`cat version.txt`
MAVEN_GOAL=$1

mvn clean

mvn ${MAVEN_GOAL} || exit 1

cd bard-archetype

# do things for every sub-directory about archetype
for DIR in `ls -d bard-*/`; do

    cd ${DIR}
    mvn archetype:create-from-project -Darchetype.keepParent=false
    cd target/generated-sources/archetype

    SED_POM_CMD="s/<groupId>com.bardframework<\/groupId>/<parent><groupId>com.bardframework<\/groupId><artifactId>bard<\/artifactId><version>$VERSION<\/version><\/parent>/g"
    SED_ARCHETYPE_POM_CMD="s/org.apache.saltedpeanuts/\${package}/g"
    SED_JAVA_CMD="s/\${groupId}/com.bardframework/g"

    if [ `uname` = 'Linux' ]; then
    	find ./src -type f ! -name "pom.xml" | xargs sed -i "$SED_JAVA_CMD"
    	sed -i "$SED_POM_CMD" pom.xml
    	sed -i "$SED_ARCHETYPE_POM_CMD" src/main/resources/archetype-resources/pom.xml
    else
    	find ./src -type f ! -name "pom.xml" | xargs sed -i '' "$SED_JAVA_CMD"
    	sed -i '' "$SED_POM_CMD" pom.xml
    	sed -i '' "$SED_ARCHETYPE_POM_CMD" src/main/resources/archetype-resources/pom.xml
    fi

    mvn ${MAVEN_GOAL}
    cd ..

done
