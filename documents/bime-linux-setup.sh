#
# this script is used to install and configure the Linux instance
# it can be used for a Stand-alone installation of Bime project
# or to configure a Amazon EC2 instance and create image.
#
#
#
##############################
# set up private key to access storage server from new instance
##############################
scp jy2947@focaplo.dnsdojo.com:/home/jy2947/.ssh/id_rsa ./

##################################
# user account "bime" which will be the account to execupte the application
##################################
sudo useradd --create-home -c "bime" bime

##################################
# create the Bime application directory in /usr/local
##################################
sudo mkdir /usr/local/bime-home
sudo mkdir /usr/local/bime-home/bime
sudo mkdir /usr/local/bime-home/bime-controller

##################################
# make bime user the owner of /usr/local/bime-home
##################################
sudo chown -R bime: /usr/local/bime-home

################################
# download the startup shellscripts
################################
cd ~
mkdir startup
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/startup ~/startup
sudo mkdir /usr/local/bime-home/startup
sudo cp ~/startup/* /usr/local/bime-home/startup
sudo chmod +x /usr/local/bime-home/*.sh
sudo chown -R bime: /usr/local/bime-home
sudo cp /usr/local/bime-home/startup/startup.sh /etc/init.d/startup.sh
sudo chmod \+x /etc/init.d/startup.sh
sudo update-rc.d startup.sh defaults

##################################
# tools
##################################
sudo apt-get -y install unzip zip curl subversion git-core
sudo apt-get -y install erlang

####################################
# install load-balancer haproxy, and configure only on the load balancer
# the initial haproxy.cfg only has default rule, www.bimelab.com -> localhost:6080
# the other rules are dynamically managed by the Python scripts with help of RabbitMq
# so, make sure to install the Pythin scripts and RabbitMq server too
# the RabbitMq server can be installed anywhere, its IP address is passed to the
# Python scripts (on the Load-balancer) through the User-Data field when creating image
# from AMI. The AMI image has a start-up shellscript to read the User-Data, and call
# the haproxy-listener python script with the RabbitMq IP address.
# This means, the RabbitMq server (if not on the same box with Load-balancer) must be
# started BEFORE the load-balance server.
# The haproxy-listener python script needs to be run by the 'sudo' to write files
# to /etc/haproxy directory and hot-reconfigure haproxy
# sudo python /usr/local/bime-home/python/focaplo/messaging/haproxy-listener.py 192.168.8.128 5672 guest guest
# 
####################################
sudo apt-get -y install haproxy
mkdir ~/haproxy
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/haproxy/haproxy.cfg ~/haproxy/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/haproxy/haproxy ~/haproxy/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/haproxy/*.template ~/haproxy/
sudo cp ~/haproxy//haproxy.cfg /etc/haproxy/haproxy.cfg
sudo cp ~/haproxy//*.template /etc/haproxy/
sudo cp ~/haproxy/haproxy /etc/default/haproxy
sudo /etc/init.d/haproxy restart

###################################
#install RabbitMQ only on the queue server
#
#RabbitMQ is another Messaging server which is developed with Erlang.
#To talk to RabbitMQ, protocol AMQP is used, the Python library is http://barryp.org/software/py-amqplib/
#http://blogs.digitar.com/jjww/2009/01/rabbits-and-warrens/
#
#
sudo -y apt-get install rabbitmq-server

###################################
# install python scripts (on both load-balancer and Bime instance)
# we use python scripts to manager below
# on load-balancer, there is a python script to listen to the RabbitMQ to add/remote/reconfigure Haproxy routing table.
# on bime server, there is a python script to listen to RabbitMQ to add/remove client (create client directory, prepare sql etc)
#
# the Python scripts reply on below external Python modules
# amqp-lib
# simplejson
cd ~
wget http://py-amqplib.googlecode.com/files/amqplib-0.6.1.tgz
tar zxvf amqplib-0.6.1.tgz
cd amqplib-0.6.1
sudo python setup.py install
wget http://pypi.python.org/packages/source/s/simplejson/simplejson-2.1.1.tar.gz#md5=0bbe3a2e5e4cac040013733aca159d89
tar zxvf simplejson-2.1.1.tar.gz
cd  simplejson-2.1.1
sudo python setup.py install
sudo mkdir /usr/local/bime-home/python
sudo scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/python /usr/local/bime-home/python
sudo chown -R bime: /usr/local/bime-home/python

#########################################
# install Subversion only on the subversion server (one time)
#########################################
sudo apt-get -y install subversion libapache2-svn
svnadmin create ~/svn-repository
sudo chown -R www-data:www-data ~/svn-repository
sudo htpasswd -c /etc/subversion/passwd jy2947
#if add user to existing hapasswd file, remove the '-c'
#sudo htpasswd /etc/subversion/password user_name


#######################################
# install tsung, optional for load testing
#######################################
cd ~
sudo apt-get -y install erlang
sudo apt-get -y install erlang-nox
sudo apt-get -y install gnuplot-nox
sudo apt-get -y install libtemplate-perl libhtml-template-perl libhtml-template-expr-perl
wget http://tsung.erlang-projects.org/dist/ubuntu/tsung_1.3.1-1_all.deb
sudo dpkg -i tsung_1.3.1-1_all.deb

##################################################
# apache and configure, on contorller server, and SVN server (required)
# Usually controller server needs Apache, and Bime doesnt need so.
#################################################
sudo apt-get -y install apache2 libapache2-mod-jk
sudo a2enmod jk proxy proxy_http ssl
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/apache/ports.conf ~/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/apache/default ~/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/apache/default-ssl ~/
#only on the SVN server
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/apache/svn ~/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/apache/tsung ~/
sudo cp ~/ports.conf /etc/apache2/ports.conf
sudo cp ~/default /etc/apache2/sites-available/default
sudo cp ~/default-ssl /etc/apache2/sites-available/default-ssl
#only on the SVN server
sudo cp ~/svn /etc/apache2/sites-available/svn
sudo cp ~/tsung /etc/apache2/sites-available/tsung
sudo a2ensite default
# not now sudo a2ensite default-ssl
sudo a2ensite svn
sudo a2ensite tsung
sudo /etc/init.d/apache2 restart

##################################################
# JDK (all servers)
##################################################
sudo apt-get -y install openjdk-6-jdk

##################################################
# MySQL on Controller server, Bime server, JIRA server
##################################################
sudo apt-get install mysql-server mysql-client
(manually enter root password G04theau)

##################################################
# Memcached on Controller server, Bime server
##################################################
sudo apt-get -y install memcached

#################################################
# tomcat6-1 to host Bime app, configured, owned by bime, only on the bime server
# the special configuration in the Tomcat zip includes:
# JAVA_HOME setted in catalina.sh
# removed all the webpps
# /conf/Catalina/localhost/ROOT.xml point to /usr/local/bime-home/bime
# the Tomcat6-1 directory is owned by user 'bime'
# and the 'tomcat6-1' script is to run tomcat with user 'bime'
#################################################
cd ~/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/tomcat-bime-6.0.20.tar.gz ~/tomcat-bime-6.0.20.tar.gz
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/tomcat6-1 ~/tomcat6-1

cd /usr/local
sudo mkdir tomcat6-1
cd /usr/local/tomcat6-1
sudo cp ~/tomcat-bime-6.0.20.tar.gz ./
sudo tar zxvf tomcat-bime-6.0.20.tar.gz
sudo chown -R bime: /usr/local/tomcat6-1
sudo cp ~/tomcat6-1 /etc/init.d/tomcat6-1
sudo chmod \+x /etc/init.d/tomcat6-1
sudo update-rc.d tomcat6-1 defaults
# 
# no need for now sudo /etc/init.d/tomcat6-1 start
#################################################
# Only on the server to send email locally
# Exim4
# the special setting is to use Gmail account to rely emails
# jy2947@gmail.com
#################################################
cd ~/
sudo apt-get -y install exim4
sudo dpkg-reconfigure exim4-config
#click OK
#Choose mail sent by smarthost; received via SMTP or fetchmail
#Type System Mail Name: focaplo.com, Tab to OK, enter
#OK enter
#enter 127.0.0.1, Tab to OK, enter
#empty other destination, Tab to OK, enter
#empty marches to replay mail for, Tab to OK, enter
#enter outgoing smartest "smtp.gmail.com::587", tab to OK, enter
#choose no, enter
#OK, enter
#choose No for "keep number of DNS minimal", enter
#choose "box format…", Tab to OK, enter
#choose No to "Split signature" enter
#OK enter
#empty post-master, Tab to OK, enter
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/exim4/exim4.conf.template ~/
sudo cp ~/exim4.conf.template /etc/exim4/
sudo update-exim4.conf
sudo /etc/init.d/exim4 restart
#################################################
# Ant is used to run the build script, only on the build-server
# it is owned by bime
# so, when running the /usr/local/bime-home/bime/build.sh, need to 'sudo su - bime' first
#################################################
#cd /usr/local
#sudo wget http://archive.apache.org/dist/ant/binaries/apache-ant-1.7.1-bin.zip
#sudo unzip apache-ant-1.7.1-bin.zip
#sudo chown -R bime: /usr/local/apache-ant-1.7.1
sudo apt-get install ant

#################################################
# Bime build and deploy shellscript, on Bime server, Build server
#################################################
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/bime/build.sh ~/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/bime/deploy.sh ~/
sudo cp ~/build.sh /usr/local/bime-home/bime/
sudo cp ~/deploy.sh /usr/local/bime-home/bime/
sudo chmod \+x /usr/local/bime-home/bime/*.sh

#################################################
#change everything under bime
sudo chown -R bime: /usr/local/bime-home
#################################################

 
