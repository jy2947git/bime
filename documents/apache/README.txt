Apache2 is used to serve the SVN server, and Tsung
Port 6080

sudo apt-get -y install apache2 libapache2-mod-jk
sudo a2enmod jk proxy proxy_http ssl
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/apache/ports.conf ~/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/apache/default ~/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/apache/default-ssl ~/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/apache/svn ~/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/apache/tsung ~/
sudo cp ~/ports.conf /etc/apache2/ports.conf
sudo cp ~/default /etc/apache2/sites-available/default
sudo cp ~/default-ssl /etc/apache2/sites-available/default-ssl
sudo cp ~/svn /etc/apache2/sites-available/svn
sudo cp ~/tsung /etc/apache2/sites-available/tsung
sudo a2ensite default
sudo a2ensite default-ssl
sudo a2ensite svn
sudo a2ensite tsung
sudo /etc/init.d/apache2 restart