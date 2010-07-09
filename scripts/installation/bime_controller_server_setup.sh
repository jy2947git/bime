#!/bin/bash
# setup Bime Controller server on top of bare server created by "bare_server_setup.sh"
# create bime user and directory
sudo useradd --create-home -c "bime" bime
sudo mkdir -p /usr/local/bime-home/bime-controller
sudo chown -R bime: /usr/local/bime-home

##################################################
# apache
#################################################
refer to ../apache/README



###################################
# rabbitMq
#################################################
refer to ../rabbitMq/REDME

#################################################
# Exim4 
#################################################
refer to ../exim4/README

#####################################################################
# install Bime Cloud-Management Python scripts
#####################################################################
refer to ../../cloud-management/install.sh

####################################
# haproxy, listener requires the cloud-management Python module
####################################
refer to ../haproxy/README
scp jy2947@distribution.bimelab.com:~/distribution/bime-controller/start_haproxy_listener.sh ./
chmod +x ~/*.sh


