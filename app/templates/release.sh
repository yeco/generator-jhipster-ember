#!/bin/sh

set -e

echo '----> Cleanup...'
./gradlew clean
grunt clean
grunt build
git add -A .

echo '----> Bumping version...'
grunt bump-only:minor

VERSION=$(awk '/version: (.*)$/ {print $2}' VERSION)
echo '----> Build version' $VERSION
