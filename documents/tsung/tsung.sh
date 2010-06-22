#!/bin/bash
echo "tsung file: $1"
~/tsung-1.3.1/tsung.sh -f $1 start
echo "generating reports..."
cd ~/.tsung/log
lastfile=`ls -t|sed q`
cd $lastfile
/usr/lib/tsung/bin/tsung_stats.pl
echo "done"
