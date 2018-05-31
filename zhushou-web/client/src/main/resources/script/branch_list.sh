#!/bin/bash
#branch_list
codePath=$1
cd ${codePath}
git pull
git fetch -p
echo "branchList"
git branch -a
echo "endBranchList"
