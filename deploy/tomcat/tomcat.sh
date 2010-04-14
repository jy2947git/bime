#!/bin/sh
#
# /etc/init.d/tomcat
#
# This is the init script for starting up the
#  Jakarta Tomcat server
#
# description: Starts and stops the Tomcat daemon.
#

# When running using service, the environmental variables are messed
# up so let us explicitly source it.  This is OS-specific; in this
# case, the line below is specific to SUSE Linux's root user profile.
#source /etc/profile

tomcat=/usr/local/tomcat6-1
startup=$tomcat/bin/startup.sh
shutdown=$tomcat/bin/shutdown.sh

# Before starting, stop any existing tomcat instance
start() {
echo starting tomcat...
    sh $startup
}


stop() {
  echo -n $"Stopping Tomcat service: "
  kill -9 `ps -ef | grep tomcat | grep -v grep| awk '{print $2}'`
}

restart() {
  stop
  sleep 5
  start
}

status() {
  ps -ef | grep tomcat | grep -v grep
}

# See how we were called.
case "$1" in
start)
  start
  ;;
stop)
  stop
  ;;
status)
  status
  ;;
restart)
  restart
  ;;
*)
  echo $"Usage: $0 {start|stop|status|restart}"
  exit 1
esac

exit 0

