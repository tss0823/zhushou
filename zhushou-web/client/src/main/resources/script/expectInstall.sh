#!/usr/bin/expect -f  
set ip [lindex $argv 0 ]     
set target_file [lindex $argv 1 ]  
set tomcat_home [lindex $argv 2 ]  
set user [lindex $argv 3 ]  
set password [lindex $argv 4 ]  
set str_d [lindex $argv 5 ]  
set tomcat_webapp_dir "${tomcat_home}/webapps/ROOT" 
set rootZip "${tomcat_home}/webapps/ROOT.zip" 
#set str_d [exec date "+%Y%m%d_%H%M_%S"]
puts "back_ver=${str_d},${ip}"
set rootZip "${tomcat_home}/webapps/ROOT.zip"
#remote copy
puts " scp ${target_file} ${user}@$ip:${rootZip}"
spawn scp "${target_file}" "${user}@$ip:${rootZip}"
#send "${password}"
expect {  
"*yes/no" { send "yes\r"; exp_continue}  
"*assword:" { send "$password\r" }  
}  
#set timeout 1
expect "100%"  
spawn ssh ${user}@$ip      
expect {  
"*yes/no" { send "yes\r"; exp_continue}  
"*assword:" { send "$password\r" }  
}  
expect "$*"  

#send "mkdir ${tomcat_home}/bak\r"
set app_bak_dir "${tomcat_home}/bak"
if {![file exists ${app_bak_dir} ]} {
      send "mkdir ${app_bak_dir}\r"
      puts "mkdir ${app_bak_dir}"
} 
send "mv -i '${tomcat_webapp_dir}' '${tomcat_home}/bak/ROOT_${str_d}.BAK'\r"
send "unzip -q ${rootZip} -d ${tomcat_webapp_dir}\r"
send "rm -fr ${rootZip}\r"
send "exit\r"
expect eof

