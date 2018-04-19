#!/bin/bash
#branch_list
codeName=$1
cd /u01/code/${codeName}
git pull
git fetch -p
echo "branchList"
git branch -a
echo "endBranchList"
