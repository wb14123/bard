#!/bin/sh

mvn clean

mvn deploy || exit 1
cd bard-archetype
mvn archetype:create-from-project -Darchetype.keepParent=false -Darchetype.postPhase=deploy
