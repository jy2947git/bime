#!/bin/bash
/usr/local/apache-ant-1.7.1/bin/ant -propertyfile ./client.properties -Dbuild.base.dir=./build -Ddist.base.dir=./dist -Dj2ee.lib=/usr/local/tomcat6-1/lib -f ./source/bime/build.xml packageDist
 
