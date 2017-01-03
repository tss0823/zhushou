#!/bin/bash
#
module=$1
rm -fr /u01/deploy/project/tomcat_zhushou_${module}/webapps/*
echo "cp -r /u01/code/zhushou/zhushou-web/${module}/target/${module} /u01/deploy/project/tomcat_zhushou_${module}/webapps/ROOT"
cp -r /u01/code/zhushou/zhushou-web/${module}/target/${module} /u01/deploy/project/tomcat_zhushou_${module}/webapps/ROOT

