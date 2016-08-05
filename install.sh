#!/bin/bash
#
module=$1
module_dir="deploy"
src_dir="deploy"
case "$module" in
    deploy)
        src_dir="deploy"
        module_dir="deploy"

        ;;
    task1)
        src_dir="task"
        module_dir="task1"

        ;;
    task2)
        src_dir="task"
        module_dir="task2"

        ;;
    task3)
        src_dir="task"
        module_dir="task3"
        ;;

       *)
        echo "usage [deploy|task1|task2|task3]"
        exit 0
        ;;
esac
rm -fr /u01/deploy/project/tomcat_${module_dir}/webapps/*
echo "cp -r zhushou-${src_dir}/target/zhushou-${src_dir} /u01/deploy/project/tomcat_${module_dir}/webapps/ROOT"
cp -r zhushou-${src_dir}/target/zhushou-${src_dir} /u01/deploy/project/tomcat_${module_dir}/webapps/ROOT

