#!/bin/bash
#
# this script will download the source code from SVN server and run the Ant build script to generate the distribution zip file
# make sure Ant is already installed "sudo apt-get install ant"
# make sure SVN is installed "sudo apt-get install subversion"
#
#
if [ -d source ]; then
echo 'find source directory'
else
echo 'creating source directory'
mkdir source
fi
cd source
echo 'download file from svn....'
svn --username jy2947 --password jy2947 --non-interactive checkout --force http://svn.bimelab.com/svn/svn-repository/bime
echo 'running build script'
ant -f bime/build.xml
cp /usr/local/bime-home/bime/source/bime/dist/bime/bime.zip /usr/local/bime-home/bime
exit 0

