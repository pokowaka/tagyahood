#!/bin/bash
# This will update the subtrees
git subtree pull --prefix android/openpaper git@github.com:pokowaka/openpaper.git master --squash
git subtree pull --prefix android/mapbox git@github.com:pokowaka/mapbox-android-sdk.git master --squash
