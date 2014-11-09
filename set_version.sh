#!/bin/sh

VERSION=$1

mvn versions:set -DnewVersion=$VERSION
echo $VERSION > version.txt
