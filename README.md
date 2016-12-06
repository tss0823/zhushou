逆袭学院助手管理平台

###使用说明
####编译打包
sh /u01/deploy/script/deploy.sh package,yuntao-im,prod,prod

####发布
sh /u01/deploy/script/deploy.sh run,yuntao-im im 10.174.110.159,10.174.108.45

####静态发布
sh /u01/deploy/script/deploy.sh install,yuntao-im im 10.174.110.159,10.174.108.45

####重启
sh /u01/deploy/script/deploy.sh restart,yuntao-im im 10.174.110.159,10.174.108.45

####上线
sh /u01/deploy/script/deploy.sh start,yuntao-im im 10.174.110.159,10.174.108.45

####下线
sh /u01/deploy/script/deploy.sh stop,yuntao-im im 10.174.110.159,10.174.108.45

