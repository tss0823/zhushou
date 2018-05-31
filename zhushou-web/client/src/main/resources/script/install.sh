#!/bin/sh
# install
function trans(){
codeName=$1 #代码git名称 health
appName=$2  #应用服务名称 user
#publishName=$3 #发布应用名称 search-web
ip=$3
user=$4
pwd=$5
back_ver=$6
deployShellBaseBir=$7
deployCodeDir=$8
deployWebDir=$9
target_base_dir="${deployCodeDir}/${codeName}/${codeName}-web/${appName}/target"
target_file="${target_base_dir}/${appName}.war"

#trans start
tomcat_home="${deployWebDir}/tomcat_${appName}"


#remote scp and unzip file
/usr/bin/expect ${deployShellBaseBir}/expectInstall.sh $ip $target_file $tomcat_home $user $pwd $back_ver


echo "trans ${appName} ${ip} finished!"
}

trans $1 $2 $3 $4 $5 $6 

