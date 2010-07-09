#!/bin/bash
#
# this script is to start the Haproxy listener (python script)
# it requires below arguments: host, port, user, password
# 
sudo python /usr/local/bime-home/focaplo-1.0/python/focaplo/messaging/haproxy_listener.py 192.168.8.130 5672 guest guest >> ~/haproxy_listener.log &
