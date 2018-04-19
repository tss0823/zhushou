#!/bin/bash
#deploy ios
paramNum=$#
appName=$1
br=$2
model=$3
version=$4
outputPath=$5
fileName=$6

if [ $paramNum -lt 5 ]
then
echo "useage ./deploy_IOS.sh appName frontModel version outputPath fileName"
exit 0
fi

#package 
source_dir="/u01/code/android/${appName}" 
echo "cd ${source_dir}" 
cd ${source_dir} 
#pull new branch 
git pull 
cmd_co="git checkout ${br}" 
eval ${cmd_co} 
git pull 
frontModel="$(echo ${model} | sed 's/^\(.\)/\u\1/')"
echo "frontModel=${frontModel}"
echo "/u01/gradle-3.3/bin/gradle greendao assemble${frontModel} -PAPP_VERSION=${version} -POUT_PUT_DIR=${outputPath} -PFILE_NAME=${fileName}"
#/u01/gradle-3.3/bin/gradle assemble${frontModel} -PAPP_VERSION=${version} -POUT_PUT_DIR=${outputPath} -PFILE_NAME=${fileName}
if (( $? ))  
then  
    echo -e "发布失败"
    exit 1 
else  
    echo -e "发布成功"
    exit 0 
fi 
