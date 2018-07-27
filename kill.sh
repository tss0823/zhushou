#!/bin/bash
#
pid=`ps aux | grep zhushou | grep -v grep | awk '{print $2}'`
echo "kill ${pid}"
kill ${pid}
