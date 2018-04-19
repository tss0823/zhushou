#!/usr/bin/expect -f  
set fileName "/u01/hongbao/test_2"
set password "Tqp1928admin" 
spawn ssh hongbao@10.174.110.159
expect {  
"*yes/no" { send "yes\r"; exp_continue}  
"*password:" { send "$password\r" }  
}  
expect "$*"  
if { [file exist $fileName] } {
        puts "file exists"
} else {
        puts " not exists"
       send "mkdir ${fileName}\r"
}
#send "mkdir ${tomcat_home}/bak\r"
send "exit\r"
expect eof

