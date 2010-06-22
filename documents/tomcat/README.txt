#
#We use Tomcat 6 as application server to serve Bime and BimeController.
#There are 2 installs of Tomcat under /usr/local

#Tomcat6-1: Bime Port 8080
#Tomcat6-0: BimeController Port:8090
#Tomcat application directory should be owned by user "bime" so web app can write to the user directory
#Below is the steps to install and configure Tomcat 6-1

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
# no need for now sudo /etc/init.d/tomcat6-1 start






