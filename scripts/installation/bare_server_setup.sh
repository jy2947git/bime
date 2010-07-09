#!/bin/bash
# setup a bare Ubuntu Lucid server with Apache, MySql, Java
#set -e -x
#export DEBIAN_FRONTEND=noninteractive
sudo apt-get -y update
sudo apt-get upgrade -y

sudo apt-get -y install ssh unzip zip curl subversion git-core ant erlang
# Sun Java
sudo apt-get -y install python-software-properties
sudo add-apt-repository "deb http://archive.canonical.com/ lucid partner"
sudo apt-get update
sudo apt-get -y install sun-java6-jdk
sudo update-java-alternatives -s java-6-sun
echo JAVA_HOME=/usr/lib/jvm/java-6-sun


