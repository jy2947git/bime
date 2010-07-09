#!/bin/bash
# setup Bime server on top of bare server created by "bare_server_setup.sh"
# create bime user and directory
sudo useradd --create-home -c "bime" bime
sudo mkdir -p /usr/local/bime-home/bime
sudo chown -R bime: /usr/local/bime-home

#################################################
#
#
refer to ../tomcat/README

# Bime Cloud-Management Python
refer to ../../cloud-management/install.sh
scp jy2947@distribution.bimelab.com:~/distribution/bime/start_bime_client_listener.sh ./
chmod +x ~/*.sh


# memcached
refer to ../memcached/README

# mysql EBS re-configuration
refer to ../mysql/README

# Bime App
scp jy2947@distribution.bimelab.com:~/distribution/bime/bime.zip ~/
scp jy2947@distribution.bimelab.com:~/distribution/bime/deploy.sh ~/
sudo cp ~/deploy.sh /usr/local/bime-home/bime/
sudo chmod \+x /usr/local/bime-home/bime/*.sh
sudp cp ~/bime.zip /usr/local/bime-home/bime/
cd /usr/local/bime-home/bime
sudo ./deploy.sh 2






