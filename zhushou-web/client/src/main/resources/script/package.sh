#!/bin/sh
# packages
codeName=$1
if [ -z $codeName ]
then
 echo "git codeName  must not be null"
 exit 0
fi

br=$2
if [ -z $br ]
then
 echo "git br must not be null"
 exit 0
fi

model=$3
if [ -z $model ]
then
 echo "model must not be null"
 exit 0
fi

compileProperty=$4
deployCodeDir=$5

#package
source_dir="${deployCodeDir}/${codeName}"
echo "cd ${source_dir}"
cd ${source_dir}
#pull new branch
git pull
cmd_co="git checkout ${br}"
eval ${cmd_co}
git pull
#echo "/u01/path/apache-maven-3.3.3/bin/mvn clean package -P ${model} -Dmaven.test.skip=true -DhongbaoAppType=hongbaoAppTypeVal ${compileProperty} -U"
/u01/path/apache-maven-3.3.3/bin/mvn clean package -P ${model} -Dmaven.test.skip=true -DhongbaoAppType=hongbaoAppTypeVal ${compileProperty} -U
if (( $? )) 
then 
    echo -e "\033[31m 编译打包失败 \033[0m"
    exit 1
else 
    echo -e "\033[32m 编译打包成功 \033[0m"
    exit 0
fi
