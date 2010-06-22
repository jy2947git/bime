#
#haproxy is used here as load balancer
#
#
#To install and configure
#
sudo apt-get -y install haproxy
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/haproxy/haproxy.cfg ~/
scp -i ~/id_rsa jy2947@focaplo.dnsdojo.com:/home/jy2947/bime-home/haproxy/haproxy ~/
sudo cp ~/haproxy.cfg /etc/haproxy/haproxy.cfg
sudo cp ~/haproxy /etc/default/haproxy
sudo /etc/init.d/haproxy restart