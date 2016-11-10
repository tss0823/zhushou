#!/bin/bash
#
module=$1
module_dir="deploy"

cd /u01/deploy/project/tomcat_zhushou
./tomcat.sh stop
sleep 5
ps aux | grep /u01/deploy/project/tomcat_zhushou/ | grep -v grep | awk '{print $2 }' | xargs kill
./tomcat.sh start
