#!/bin/bash
#
# this script will download the source code and run the Ant build script to generate the distribution zip file
# make sure Ant is already installed "sudo apt-get install ant"
# make sure Git is installed "sudo apt-get install git"
# 
# it is assuming the bime directory already exists
# One usage of this script is to build latest code on Bime instance
#

cd /usr/local/bime-home/bime
if [ -d source ]; then
echo 'find source directory'
else
echo 'creating source directory'
mkdir source
fi
cd source
echo 'download file from github....'
git clone git@github.com:jy2947git/bime.git
echo 'running build script'
ant -f bime/build.xml
cp /usr/local/bime-home/bime/source/bime/dist/bime/bime.zip /usr/local/bime-home/bime
exit 0

