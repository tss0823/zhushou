#!/bin/bash
#
module=$1

sh install.sh ${module}

cd /u01/deploy/project/tomcat_zhushou_${module}
./tomcat.sh stop
sleep 5
ps aux | grep /u01/deploy/project/tomcat_zhushou_${module}/ | grep -v grep | awk '{print $2 }' | xargs kill
./tomcat.sh start
