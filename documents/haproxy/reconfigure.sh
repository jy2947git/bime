#!/bin/bash

# save previous state
sudo  mv /etc/haproxy/haproxy.config /etc/haproxy/haproxy.config.old
sudo  mv /var/run/haproxy.pid /var/run/haproxy.pid.old

sudo mv /etc/haproxy/haproxy.config.new /etc/haproxy/haproxy.config
sudo kill -TTOU $(cat /var/run/haproxy.pid.old)
 
 if haproxy -p /var/run/haproxy.pid -f /etc/haproxy/haproxy.config; then
    echo "New instance successfully loaded, stopping previous one."
    sudo kill -USR1 $(cat /var/run/haproxy.pid.old)
    sudo rm -f /var/run/haproxy.pid.old
    exit 1
  else
    echo "New instance failed to start, resuming previous one."
    sudo kill -TTIN $(cat /var/run/haproxy.pid.old)
    sudo rm -f /var/run/haproxy.pid
    sudo mv /var/run/haproxy.pid.old /var/run/haproxy.pid
    sudo mv /etc/haproxy/haproxy.config /etc/haproxy/haproxy.config.new
    sudo mv /etc/haproxy/haproxy.config.old /etc/haproxy/haproxy.config
    exit 0
  fi
