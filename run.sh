#!/bin/bash
#
paramNum=$#
if [ $paramNum -lt 1 ]
then
echo "usage ./run.sh deploy or ./run.sh client"
exit 0
fi
projectDir=`pwd`
module=$1
cd ${projectDir}/zhushou-web/${module}
pwd
echo "start ${module} ..."
mvn tomcat7:run -Dbase.home=${projectDir}> ${projectDir}/logs/zhushou.log 2>&1 &
