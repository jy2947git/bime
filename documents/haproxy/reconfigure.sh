#!/bin/bash
# this script assumes the new configuration is already set up in the /etc/haproxy.cfg file
# save previous state
#sudo  mv /etc/haproxy/haproxy.cfg /etc/haproxy/haproxy.cfg.old
#sudo  mv /var/run/haproxy.pid /var/run/haproxy.pid.old
#
#sudo mv /etc/haproxy/haproxy.cfg.new /etc/haproxy/haproxy.cfg
#sudo kill -TTOU $(cat /var/run/haproxy.pid.old)
# 
# if haproxy -p /var/run/haproxy.pid -f /etc/haproxy/haproxy.cfg; then
#    echo "New instance successfully loaded, stopping previous one."
#    sudo kill -USR1 $(cat /var/run/haproxy.pid.old)
#    sudo rm -f /var/run/haproxy.pid.old
#    exit 1
#  else
#    echo "New instance failed to start, resuming previous one."
#    sudo kill -TTIN $(cat /var/run/haproxy.pid.old)
#    sudo rm -f /var/run/haproxy.pid
#    sudo mv /var/run/haproxy.pid.old /var/run/haproxy.pid
#    sudo mv /etc/haproxy/haproxy.cfg /etc/haproxy/haproxy.cfg.new
#    sudo mv /etc/haproxy/haproxy.cfg.old /etc/haproxy/haproxy.cfg
#    exit 0
#  fi


sudo /etc/init.d/haproxy reload
exit 0