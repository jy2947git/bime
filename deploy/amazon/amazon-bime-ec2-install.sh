echo scp jy2947@focaplo.dnsdojo.com:/home/jy2947/.ssh/id_rsa ./

sudo apt-get update
sudo apt-get -y upgrade
sudo apt-get install unzip zip curl
################################
## install mysql
################################
read -p "Press any key to continue..."
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
read -p "press any key to continue..."
#################################
##     TOMCAT set up
#################################
cd /usr/local
sudo wget http://apache.securedservers.com/tomcat/tomcat-6/v6.0.20/bin/apache-tomcat-6.0.20.zip
sudo unzip apache-tomcat-6.0.20.zip
mv apache-tomcat-6.0.20 tomcat6-1
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/tomcat6-1/conf/server.xml ./tomcat6-1/conf
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/tomcat6-1/bin/catalina.sh ./tomcat6-1/bin      
sudo rm -rf ./tomcat6-1/webapps/*
sudo mkdir -p ./tomcat6-1/conf/Catalina/localhost
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/tomcat6-1/conf/Catalina/localhost/*.xml ./tomcat6-1/conf/Catalina/localhost

read -p "press any key to continue..."
###########################
# jdbc driver
##########################
cd /usr/local
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/mysql*.* ./
sudo cp mysql*.* ./tomcat6-1/lib
read -p "press any key to continue..."
############################
## install ant
############################
cd /usr/local
sudo wget http://archive.apache.org/dist/ant/binaries/apache-ant-1.7.1-bin.zip
sudo unzip apache-ant-1.7.1-bin.zip


###############################
## bime
################################
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/bime/bime.zip  ./bime
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/bime/deploy.sh  ./bime
sudo chmod +x ./bime/*.sh
cd bime
sudo ./deploy.sh 2
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
sudo a2enmod ssl
#############################
# configure apache
############################
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/ssl/certs/server.crt /etc/ssl/certs/server.crt
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/ssl/private/server.key /etc/ssl/private/server.key
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/apache2/workers.properties-bime-only /etc/apache2/workers.properties
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/apache2/mods-available/jk.load /etc/apache2/mods-available
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/apache2/sites-available/default-bime-only /etc/apache2/sites-available/default
################################
# add auto start script to boot
# it take care any service which is not already in the boot up, for example,
# tomcat
###############################
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/start_up_services_at_boot_for_bime.sh /usr/local/start_up_services_at_boot_for_bime.sh
sudo chmod +x /usr/local/*.sh
sudo cp /usr/local/start_up_services_at_boot_for_bime.sh /etc/init.d/start_up_services_at_boot_for_bime.sh
sudo update-rc.d start_up_services_at_boot_for_bime.sh defaults

############################
# start up
############################
sudo /usr/local/tomcat6-1/bin/startup.sh
sudo /etc/init.d/apache2 restart

