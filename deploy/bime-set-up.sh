##############################
# set up private key to access this server
# from remote
##############################
# scp jy2947@focaplo.dnsdojo.com:/home/jy2947/.ssh/id_rsa ./

sudo apt-get update
sudo apt-get -y upgrade
sudo apt-get install unzip zip curl
################################
## install mysql
################################
sudo apt-get install mysql-server mysql-client
### root password G04theau
sudo mkdir /usr/local/bime-home
sudo mkdir /usr/local/bime-home/bime
sudo mkdir /usr/local/bime-home/bime-controller
sudo chown -R ubuntu:ubuntu /usr/local/bime-home
#################################
#install open jdk
sudo apt-get install openjdk-6-jdk
#java libary in /usr/lib/jvm
################################

#################################
##     TOMCAT set up
#################################
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/apache-tomcat-6.0.20.tar /usr/local/bime-home
cd /usr/local/bime-home
tar xvf apache-tomcat-6.0.20.tar
mv apache-tomcat-6.0.20 tomcat6-0
tar xvf apache-tomcat-6.0.20.tar
mv apache-tomcat-6.0.20 tomcat6-1
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/tomcat6-0/bin/catalina.sh ./tomcat6-0/bin 
rm -rf ./tomcat6-0/webapps/*
mkdir -p ./tomcat6-0/conf/Catalina/localhost
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/tomcat6-0/conf/Catalina/localhost/*.xml ./tomcat6-0/conf/Catalina/localhost
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/tomcat6-0/conf/server.xml ./tomcat6-0/conf
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/etc/init.d/tomcat6-0 /etc/init.d
sudo chmod 755 /etc/init.d/tomcat6-0
sudo update-rc.d tomcat6-0 defaults
##########################
# set up tomcat6-1
#########################
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/tomcat6-1/conf/server.xml ./tomcat6-1/conftomcat6-1
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/tomcat6-1/bin/catalina.sh ./tomcat6-1/bin      
rm -rf ./tomcat6-1/webapps/*
mkdir -p ./tomcat6-0/conf/Catalina/localhost
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/tomcat6-1/conf/Catalina/localhost/*.xml ./tomcat6-1/conf/Catalina/localhost
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/tomcat6-1/conf/server.xml ./tomcat6-1/conf
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/etc/init.d/tomcat6-1 /etc/init.d
sudo chmod 755 /etc/init.d/tomcat6-1
sudo update-rc.d tomcat6-1 defaults

###########################
# jdbc driver
##########################
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/mysql*.* ./
cp mysql*.* ./tomcat6-0/lib
cp mysql*.* ./tomcat6-1/lib

############################
## install ant
############################
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/apache-ant-1.7.1-bin.tar ./
tar xvf apache-ant-1.7.1-bin.tar


###############################
## bime and bime-controller set up
################################
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/bime/bime.zip  ./bime
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/bime/deploy.sh  ./bime
sudo chmod +x ./bime/*.sh
cd bime
./deploy.sh 2
################################
# bime-controller set up
###############################
cd ..
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/bime-controller/bime-controller.zip  ./bime-controller
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/bime-controller/build.sh  ./bime-controller
sudo chmod +x ./bime-controller/*.sh
cd bime-controller
./build.sh
cd scripts
sudo chmod +x *.sh
./refreshMysql.sh
cd ..
cd ..

#############################
## apache set up
#############################
sudo apt-get install apache2
sudo apt-get install libapache2-mod-jk
sudo a2enmod jk
sudo a2enmod proxy
sudo a2enmod proxy_http
#############################
# configure apache
############################
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/apache2/workers.properties /etc/apache2
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/apache2/mods-available/jk.load /etc/apache2/mods-available
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/apache2/sites-available/default /etc/apache2/sites-available
############################
# start up
############################
./tomcat6-0/bin/startup.sh
./tomcat6-1/bin/startup.sh
sudo /etc/init.d/apache2 restart

