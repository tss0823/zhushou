#!/bin/bash
#
module=$1
module_dir="deploy"
case "$module" in
    deploy)
        module_dir="deploy"

        ;;
    task1)
        module_dir="task1"

        ;;
    task2)
        module_dir="task2"

        ;;
    task3)
        module_dir="task3"
        ;;

       *)
        #echo "usage [logstash|zookeeper|solr]"
        echo "usage [deploy|task1|task2|task3]"
	exit 0
        ;;
esac

cd /u01/project/hongbao_${module_dir}
./tomcat.sh stop
sleep 5
ps aux | grep /u01/project/hongbao_${module_dir}/ | grep -v grep | awk '{print $2 }' | xargs kill
./tomcat.sh start
