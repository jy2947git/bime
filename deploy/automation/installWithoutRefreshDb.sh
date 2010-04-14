#!/bin/bash
echo "first time to install the app for $1"
mkdir /usr/local/$1
mv /usr/local/$1.zip ./$1
cd $1
unzip $1.zip
echo "decompressing the war file..."
cd /usr/local/$1/www
jar xvf $1.war
rm -f $1.war
echo "setting tomcat..."
cd /usr/local/$1/server
cp *.xml /usr/local/tomcat6-1/conf/Catalina/localhost
echo "done"

