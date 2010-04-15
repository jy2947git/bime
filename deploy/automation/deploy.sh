#!/bin/bash
#
# this script will deploy the bime web application with/out refreshing mysql database.
#
#
#
#


echo install db option is $refreshDb
echo check ./www directory, as the web root
rm -rf ./www
mkdir ./www
echo grab the distribution zip file
mv ./source/bime/dist/bime/bime.zip ./
unzip -o bime.zip
echo decompressing the war file
cd www
jar xvf bime.war
rm -f bime.war
echo pre-compiling jsp files...
/usr/local/apache-ant-1.7.1/bin/ant -Dtomcat.home=/usr/local/tomcat6-1 -Dwebapp.path=./ -f precompile_jsp.xml
if [ $1 -gt 0 ]
then
echo "recreating database...."
cd ../scripts
chmod +x *.sh
./refreshMysql.sh
fi
exit 1
