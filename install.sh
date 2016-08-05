#!/bin/bash
#
module=$1
module_dir="deploy"
src_dir="zhushou"
rm -fr /u01/deploy/project/tomcat_zhushou/webapps/*
echo "cp -r ${src_dir}_$/target/zhushou-${src_dir} /u01/deploy/project/tomcat_${module_dir}/webapps/ROOT"
cp -r zhushou-deploy/target/zhushou-deploy /u01/deploy/project/tomcat_zhushou/webapps/ROOT

