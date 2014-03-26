#!/bin/bash
# This will update the subtrees
git subtree push --prefix android/openpaper git@github.com:pokowaka/openpaper.git master --squash
git subtree push --prefix android/mapbox git@github.com:pokowaka/mapbox-android-sdk.git master --squash
