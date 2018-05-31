#!/bin/bash
#git batch execute
# git commit and push

#execute function
function exec_cmd(){
e_cmd=$@
echo "exec cmd=${e_cmd}";
eval ${e_cmd}
#if [ $? != 0 ]
#then
#echo "execute ${e_cmd} failed!"
#exit 0
#fi
}

#main start
paramNum=$#
#if [ $paramNum -lt 2 ]
#then
#echo "usage merge from_branch to_branch "
#exit 0
#fi

#from_br=$1
#to_br=$2

#cmd_co_dev="git checkout ${from_br}"

#check dev branch
#exec_cmd ${cmd_co_dev}
git pull

#git add . & git commit -a
cmd_add_and_ci="git add . && git commit -m 'modified some code'";
exec_cmd ${cmd_add_and_ci}
git push
echo "all finished, execute successed!";
