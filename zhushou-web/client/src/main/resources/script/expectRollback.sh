#!/usr/bin/expect -f  
set timeout 30
set appName [lindex $argv 0 ]     
set ip [lindex $argv 1 ]  
set user [lindex $argv 2 ]  
set password [lindex $argv 3 ]  
set back_ver [lindex $argv 4 ]
set deployWebDir [lindex $argv 7 ]
set tomcat_home "${deployWebDir}/tomcat_${appName}"
set tomcat_webapp_dir "${tomcat_home}/webapps/ROOT" 
#remote login
spawn ssh  ${user}@$ip      
expect {  
"*yes/no" { send "yes\r"; exp_continue}  
"*assword:" { send "$password\r" }  
}  
expect "$*"  
send "ls \r"
send "rm -fr '${tomcat_webapp_dir}'\r"
send "cp -r '${tomcat_home}/bak/${back_ver}' '${tomcat_webapp_dir}'\r"
#send "sleep 2s\r"
send "exit 0\r"
expect eof
