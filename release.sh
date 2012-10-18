#!/usr/bin/env bash

set -o errexit

version=$1
[ -z $version ] && read -p "Enter release version: " version

mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$version
git commit pom.xml --verbose --message "Prepare for $version release."
git tag v$version

version=$2
[ -z $version ] && read -p "Enter development version: " version

mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$version
git commit pom.xml --verbose --message "Prepare for next development version ($version)."
