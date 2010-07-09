#!/bin/bash
#
#We use Tomcat 6 as application server to serve Bime and BimeController.
#There are 2 installs of Tomcat under /usr/local
#
#Tomcat6-1: Bime Port 8080
#Tomcat6-0: BimeController Port:8090
#Tomcat application directory should be owned by user "bime" so web app can write to the user directory
#Below is the steps to install and configure Tomcat 6-1
#the installation files come from the distribution server already configured
#
#run this after bime already installed...
cd ~/
scp jy2947@distribution.bimelab.com:~/distribution/bime/tomcat/tomcat-bime-6.0.20.tar.gz ~/tomcat-bime-6.0.20.tar.gz
scp jy2947@distribution.bimelab.com:~/distribution/bime/tomcat/tomcat6-1 ~/tomcat6-1

cd /usr/local
sudo mkdir tomcat6-1
cd /usr/local/tomcat6-1
sudo cp ~/tomcat-bime-6.0.20.tar.gz ./
sudo tar zxvf tomcat-bime-6.0.20.tar.gz
sudo chown -R bime: /usr/local/tomcat6-1
sudo cp ~/tomcat6-1 /etc/init.d/tomcat6-1
sudo chmod \+x /etc/init.d/tomcat6-1
sudo update-rc.d tomcat6-1 defaults
sudo /etc/init.d/tomcat6-1 start






