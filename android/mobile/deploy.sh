#!/bin/bash
#
# cygwin / bash script
#
# adb must be in the search execution path
#
# uninstall: adb uninstall com.best.ui
#

pushd bin/
adb -s emulator-5554 install chalobest-debug.apk
popd
