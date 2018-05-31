#!/bin/bash
#deploy run=install && restart
paramNum=$#
firstParam=$1
deployShellBaseBir=`echo "${firstParam}" | awk -F ',' '{print $1}'` 
deployCodeDir=`echo "${firstParam}" | awk -F ',' '{print $2}'`
deployWebDir=`echo "${firstParam}" | awk -F ',' '{print $3}'`
deployUser=`echo "${firstParam}" | awk -F ',' '{print $4}'`
deployPwd=`echo "${firstParam}" | awk -F ',' '{print $5}'`

firstParam=$2
fun=`echo "${firstParam}" | awk -F ',' '{print $1}'` 
codeName=`echo "${firstParam}" | awk -F ',' '{print $2}'` 
branch=`echo "${firstParam}" | awk -F ',' '{print $3}'` 
model=`echo "${firstParam}" | awk -F ',' '{print $4}'` 
compileProperty=`echo "${firstParam}" | awk -F ',' '{print $5}'` 
#echo "compileProprty=${compileProperty}"
if [ $paramNum -eq 1 -a "${fun}" == "package" ];then
echo "package execute.."
sh ${deployShellBaseBir}/package.sh ${codeName} $branch ${model} "${compileProperty}" "${deployCodeDir}"
exit 0
fi

if [ $paramNum -lt 3 ]
then
echo "useage ./deploy.sh [package][install][restart][debug][start][stop][run][all][rollback],codeName,branch,model,compileProperty appName ip1,ip2"
exit 0
fi


if [ "${fun}" == "install" ];then
echo "install execute ..."
elif [ "${fun}" == "restart" ];then
echo "restart execute ..."
elif [ "${fun}" == "debug" ];then
echo "debug execute ..."
elif [ "${fun}" == "start" ];then
echo "start execute ..."
elif [ "${fun}" == "stop" ];then
echo "stop execute ..."
elif [ "${fun}" == "run" ];then
echo "run execute ..."
elif [ "${fun}" == "rollback" ];then
back_ver=${codeName}
echo "rollback execute ..."
elif [ "${fun}" == "all" ];then
echo "all execute ..."
sh ${deployShellBaseBir}/package.sh ${codeName} $branch ${model} "{compileProperty}" "${deployCodeDir}"
 if (( $? )) 
 then 
    exit 0
 fi
else
echo "useage ./deploy.sh [package][install][restart][debug][start][stop][run][all][rollback],codeName,branch,model compileProperty appName ip1,ip2"
exit 0
fi
appName=$2
echo "${codeName} ${model} ${compileProperty} ${appName}"


data_param=()
ipList=$3
ipMap=`echo "${ipList}" | sed "s/,/ /g"` 

str_d=`date "+%Y%m%d_%H%M_%S"`
pwd=${deployPwd}
user=${deployUser}


for var in $ipMap; do
ip="${var}"
echo "ip=$ip"
publishName="${appName}"
if [ "${fun}" == "install" ] || [ "${fun}" == "run" ] || [ "${fun}" == "all" ]
then
sh ${deployShellBaseBir}/install.sh ${codeName} ${appName} ${ip} ${user} ${pwd} ${str_d} ${deployShellBaseBir} ${deployCodeDir} ${deployWebDir}
fi

#restart
if [ "${fun}" == "restart" ] || [ "${fun}" == "run" ] || [ "${fun}" == "all" ]
then
/usr/bin/expect ${deployShellBaseBir}/expectRestart.sh ${appName} ${ip} ${user} ${pwd} ${deployShellBaseBir} ${deployCodeDir} ${deployWebDir}
fi

#debug
if [ "${fun}" == "debug" ]
then
/usr/bin/expect ${deployShellBaseBir}/expectDebug.sh ${appName} ${ip} ${user} ${pwd} ${deployShellBaseBir} ${deployCodeDir} ${deployWebDir}
fi

#start
if [ "${fun}" == "start" ]
then
/usr/bin/expect ${deployShellBaseBir}/expectStart.sh ${appName} ${ip} ${user} ${pwd} ${deployShellBaseBir} ${deployCodeDir} ${deployWebDir}
fi

#stop
if [ "${fun}" == "stop" ]
then
/usr/bin/expect ${deployShellBaseBir}/expectStop.sh ${appName} ${ip} ${user} ${pwd} ${deployShellBaseBir} ${deployCodeDir} ${deployWebDir}
fi

if [ "${fun}" == "rollback" ] 
then
/usr/bin/expect ${deployShellBaseBir}/expectRollback.sh ${appName} ${ip} ${user} ${pwd} ${back_ver} ${deployShellBaseBir} ${deployCodeDir} ${deployWebDir}
fi
done
