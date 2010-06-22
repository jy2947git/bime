#!/bin/bash
#
# this script will deploy the bime web application with/out refreshing mysql database.
#
# the user to run this script must own the /bime-home and tomcat
#
#


echo install db option is $refreshDb
echo check ./www directory, as the web root, note it is owned by tomcat account!
cd /usr/local/bime-home/bime
echo "delete the www directory..."
sudo rm -rf ./www
mkdir ./www
echo grab the distribution zip file
unzip -o bime.zip
echo decompressing the war file
cd www
jar xvf bime.war
rm -f bime.war
echo pre-compiling jsp files...
/usr/local/apache-ant-1.7.1/bin/ant -Dtomcat.home=/usr/local/tomcat6-1 -Dwebapp.path=./ -f precompile_jsp.xml
cd ../scripts
find . -type f -name '*.sh' -exec chmod +x {} \;
if [ $1 -gt 0 ] ; then
echo "recreating database...."
cd mysql
./refreshMysql.sh
cd ../../
fi
echo "restarting tomcat6-1"
sudo /etc/init.d/tomcat6-1 restart
exit 0

