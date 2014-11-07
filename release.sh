#!/bin/sh

mvn clean deploy -P release || exit 1

cd bard-archetype
mvn archetype:create-from-project -Darchetype.keepParent=false
cd bard-archetype/target/generated-sources
mvn clean deploy -P release

