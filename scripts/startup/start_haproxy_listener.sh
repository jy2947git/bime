#!/bin/bash
#
# this script is to start the Haproxy listener (python script)
# it requires below arguments: host, port, user, password
# 
sudo py /usr/local/bime-home/python/focaplo/messaging/haproxy_listener.py $1 $2 $3 $4 >> /var/log/haproxy_listener.log &
