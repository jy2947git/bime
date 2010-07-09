#Need to install JDK first before running Tomcat etc
.
####################################################
# Sun JDK
# Have to download the rpm.bin and use rpm to install
###################################################
sudo vi /etc/apt/sources.list.d/lucidparternet.list
# enter deb http://archive.canonical.com/ lucid partner
# or with below commands
sudo apt-get install python-software-properties
sudo add-apt-repository "deb http://archive.canonical.com/ lucid partner"

#
#
#
sudo apt-get update
sudo apt-get install sun-java6-jdk
sudo update-java-alternatives -s java-6-sun
echo JAVA_HOME=/usr/lib/jvm/java-6-sun
#
#
#
