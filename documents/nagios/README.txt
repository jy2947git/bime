#
# Nagios is used to monitor systems
#
###################################################################
# master server install
#
cd ~/
wget http://prdownloads.sourceforge.net/sourceforge/nagios/nagios-3.2.1.tar.gz
wget http://prdownloads.sourceforge.net/sourceforge/nagiosplug/nagios-plugins-1.4.14.tar.gz
sudo apt-get -y install apache2
sudo apt-get -y install php5-common libapache2-mod-php5
sudo apt-get -y install build-essential
sudo apt-get -y install libgd2-xpm-dev
#
#create user name and group
#
sudo useradd -m nagios
sudo passwd nagios
#
# enter nagios9846!@#
#
sudo groupadd nagcmd
sudo usermod -a -G nagcmd nagios
sudo usermod -a -G nagcmd www-data
#
# install Nagios
#
cd ~/
tar -zxvf nagios-3.2.1.tar.gz
cd ~/nagios-3.2.1
./configure --with-command-group=nagcmd
make all
sudo make install
sudo make install-init
sudo make install-config
sudo make install-commandmode
sudo make install-webconf

sudo htpasswd -c /usr/local/nagios/etc/htpasswd.users nagiosadmin
# enter nagios9846!@#


#
# install nagios plugin
#
cd ~/
tar -zxvf nagios-plugins-1.4.14.tar.gz
cd nagios-plugins-1.4.14
./configure --with-nagios-user=nagios --with-nagios-group=nagios
make
sudo make install
sudo ln -s /etc/init.d/nagios /etc/rcS.d/S99nagios
sudo /usr/local/nagios/bin/nagios -v /usr/local/nagios/etc/nagios.cfg
sudo /etc/init.d/nagios start

#
#to remotely talk with other nrpe service
#
sudo apt-get install nagios-nrpe-plugin

#
# autostart
#
cd /etc/init.d
sudo update-rc.d nagios defaults
#
# configure apache2 virtual host for nagios.bimelab.com
#
cd ~/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/apache/nagios ~/
sudo cp ~/nagios /etc/apache2/sites-available/nagios
cd /etc/apache2/sites-available
sudo a2ensite nagios
#
#restart apache
#
sudo /etc/init.d/apache2 restart
#
# update haproxy
# refer to the /haproxy directory and readme file
#
# restart haproxy
sudo /etc/init.d/haproxy -f /etc/haproxy/haproxy.cfg -sf

###############################################
#
#
# install the NRPE server on the remote box
#
apt-get install nagios-nrpe-server
apt-get install nagios-plugins
# put in the allowed incoming IP address (MUST BE INTERNAL ADDRESS of EC2)
sudo vi /etc/nagios/nrpe_local.cfg
sudo /etc/init.d/nagios-nrpe-server restart
#
# ????edit the check_nrpe
#
#
 /usr/lib/nagios/plugins/check_nrpe -H 10.251.199.198 -c check_users