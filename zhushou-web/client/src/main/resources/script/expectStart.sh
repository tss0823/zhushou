#!/usr/bin/expect -f  
set appName [lindex $argv 0 ]     
set ip [lindex $argv 1 ]  
set user [lindex $argv 2 ]  
set password [lindex $argv 3 ]
set deployWebDir [lindex $argv 6 ]
#remote login
spawn ssh ${user}@$ip      
expect {  
"*yes/no" { send "yes\r"; exp_continue}  
"*assword:" { send "$password\r" }  
}  
#set timeout 1
#expect "Last login"  
expect "$*"  
set tomcat_home "${deployWebDir}/tomcat_${appName}"
send "cd ${tomcat_home}\r"
#send "ls\r"
puts ${tomcat_home}
send "sh tomcat.sh start\r"
puts "${appName} restart finished!"
#send "sleep 3s\r"
send "tail -f ${tomcat_home}/logs/catalina.out\r"
send "exit 0\r"
expect eof
