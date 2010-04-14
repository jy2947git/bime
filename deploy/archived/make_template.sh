#!/bin/bash
/usr/local/apache-ant-1.7.1/bin/ant -Dcustomer.id=bime -Dcustomer.name="My Lab" -Dcustomer.tag="Leading service provider for bi-medical labs" -Dcustomer.company="3L corp" -Dbuild.base.dir=./build -Ddist.base.dir=./dist -Dj2ee.lib=/usr/local/tomcat6-1/lib -f ./source/bime/build.xml packageDist
 
