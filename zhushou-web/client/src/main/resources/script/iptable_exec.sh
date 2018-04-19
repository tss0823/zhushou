#!/usr/bin/expect -f
set ip [lindex $argv 0 ]
puts "ip=${ip}"
spawn sudo /usr/sbin/iptables -A INPUT -s  ${ip} -p tcp -j ACCEPT
expect "dfit"
send "Tqp1928user\n"
interact

# drop
#iptables -I INPUT -s 112.10.158.216 -j DROP
