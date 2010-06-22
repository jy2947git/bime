######################################
#Scribe is an open source scalable logging framework, originally developed by Facebook.
#The issue Scribe tries to solve is, in a cloud environment, you have tens or hundreds or even thousands of
#servers serving clients and each of the servers spit out lots of log messages in runtime. To monitor and understand
#a distributed, scalable system, you'd need to get all the logs from all the server instances.
#http://highscalability.com/product-scribe-facebooks-scalable-logging-system
#http://eng.kaching.com/2010/01/flexible-log-monitoring-with-scribe.html
#http://agiletesting.blogspot.com/2009/10/compiling-installing-and-test-running.html
######################################

sudo apt-get -y install g++ make build-essential flex bison libtool mono-gmcs libevent-dev
sudo apt-get -y install libboost*1.40*

######################################
# Thrift
######################################
sudo apt-get -y install git-core
sudo apt-get -y install subversion
svn co http://svn.apache.org/repos/asf/incubator/thrift/trunk thrift
sudo apt-get -y install automake
cd ~/thrift
./bootstrap.sh
./configure
make
sudo make install
######################################
# Facebook 303
######################################
cd ~/thrift/contrib/fb303
./bootstrap.sh
make
sudo make install
##################################### 
# install python module of Thrift and Fb303
# they are needed to run the scribe_ctrl script
#####################################
cd ~/thrift
cd lib/py
sudo python setup.py install
cd ~/thrift
cd contrib/fb303/py
sudo python setup.py install

######################################
# Scribe
######################################
cd ~/
git clone git://github.com/facebook/scribe.git
#scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/scribe-2.1.tar ~/
#cd ~/
#tar xvf scribe-2.1.tar
#cd scribe-2.1
cd scribe
./bootstrap.sh
make
sudo make install
sudo ldconfig
######################################
# generate Scribe Java Client code
######################################
cd ~/scribe/if
echo replace the first line with
echo include "/home/jy2947/thrift/contrib/fb303/if/fb303.thrift" (~/ doesnt work)
vi scribe.thrift
thrift --gen java scribe.thrift
echo scribe thrift java client source code is in gen-java directory

#####################################
# build Thrift java library
# rely on ant "sudo apt-get install ant" and jdk "sudo apt-get install openjdk-6-jdk"
#####################################
cd ~/thrift/lib/java
ant
echo /home/jy2947/thrift/lib/java/libthrift.jar
####################################
# build Fb303 Java library
####################################
cd ~/thrift/contrib/fb303/java
echo you may have to edit the build.xml to make sure the path to the libthrift is right, also, you may need to provide the location of slf4j-api.jar file
ant
echo /home/jy2947/thrift/contrib/fb303/java/build/lib/libfb303.jar
###################################
# build the Thrift java client jar
###################################
echo you may nee to change the path to the libthrift, fb303, and slf4j-log4j12-n.jar
javac -classpath /home/jy2947/thrift/contrib/fb303/java/build/lib/libfb303.jar:/home/jy2947/thrift/lib/java/libthrift.jar:/home/jy2947/thrift/contrib/fb303/java/slf4j-log4j12-1.5.8.jar:/home/jy2947/thrift/contrib/fb303/java/slf4j-api-1.5.8.jar scribe/thrift/*.java
jar cvf scribe_thrift_client.jar ./
echo now you can copy the 3 jar files to your java project!
######################################
# configure
######################################

mkdir ~/scribe_config
cp ~/scribe/examples/* ~/scribe_config
cp  ~/scribe_config/example1.conf ~/scribe_config/scribe.conf
echo copy one of the example config files from TOP_SCRIBE_DIRECTORY/examples/example*conf to /etc/scribe/scribe.conf -- a good one to start with is example1.conf
echo edit /etc/scribe/scribe.conf and replace file_path (which points to /tmp) to a location more suitable for your system
echo you may also want to replace max_size, which dictates how big the local files can be before they're rotated (by default it's 1 MB, which is too small -- I set it to 100 MB)
echo run scribed either with nohup or in a screen session (it doesn't seem to have a daemon mode):
######################################
# start
######################################
cd ~
nohup scribed -c ~/scribe_config/scribe.conf >>~/scribe.log &
#####################################
# stop
#######################################
cd ~/scribe_config
cp ~/scribe/examples/scribe_ctrl ./
cp ~/scribe/examples/scribe_cat ./
sudo ./scribe_ctrl stop
