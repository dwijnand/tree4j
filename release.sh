#!/bin/bash

version=$1
if [ -z $version ]; then
    read -p "Enter release version: " version
fi
mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$version
hg commit --verbose --message "pom: prepare for $version release"
hg tag --verbose v$version

version=$2
if [ -z $version ]; then
    read -p "Enter development version: " version
fi
mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$version
hg commit --verbose --message "pom: set version to $version"
