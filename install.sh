#!/bin/sh

# Install JARs to local maven.

# install all the regular projects
mvn install || exit 1

# install the generated archetype
cd bard-archetype
mvn archetype:create-from-project -Darchetype.keepParent=false -Darchetype.postPhase=install

