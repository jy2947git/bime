#!/bin/bash
#
# this script is to start the Bime Client listener (python script)
# it requires below arguments: host, port, user, password
# 
sudo python /usr/local/bime-home/python/focaplo-1.0/focaplo/messaging/client_listener.py 192.168.8.130 5672 guest guest >> ~/bime_listener.log &
