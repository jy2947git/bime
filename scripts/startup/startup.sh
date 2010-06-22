#!/bin/bash
#
# this script is hooked in the AMI to run at EC2 instance start up.
# it should be installed on all instances.
#
echo 'get user data from instance...'
echo 'internal IP:'
echo 'Queue IP:'
echo 'role:'
#
# if role is 'haproxy', then run start_haproxy_listener.sh
#
sudo /usr/local/bime-home/startup/start_haproxy_listener.sh localhost 5672 guest guest
#
# if role is 'bime', run the start_bime_client_listener.sh
#
sudo /usr/local/bime-home/startup/start_bime_client_listener.sh localhost 5672 guest guest