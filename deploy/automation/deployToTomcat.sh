#!/bin/bash
echo shutting down tomcat6-1
/usr/local/tomcat6-1/bin/shutdown.sh
echo wait for 5 seconds
sleep 5
echo remove existing files from tomcat/webapps
rm -rf /usr/local/tomcat6-1/webapps/bime
rm -rf /usr/local/tomcat6-1/work/catalina/localhost/bime
echo copying bime.war to webapps
mkdir /usr/local/tomcat6-1/webapps/bime
cp /usr/local/bime/bime.war /usr/local/tomcat6-1/webapps/bime/
echo unzip bime.war
cd /usr/local/tomcat6-1/webapps/bime
jar xvf bime.war
rm bime.war
echo start tomcat
/usr/local/tomcat6-1/bin/startup.sh
