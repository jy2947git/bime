#!/bin/bash
echo starting bime controller stuff
sudo /usr/local/broad-cast-myself.sh
sudo /usr/local/hornetq-2.0.0.GA/bin/run.sh
sudo /usr/local/tomcat6-1/bin/startup.sh
sudo /usr/local/tomcat6-0/bin/startup.sh
