#!/bin/bash
echo build for $1
#cd $1
#rm -rf source
mkdir source
cd source
svn checkout http://localhost/svn/bime
cd ..
./make.sh
echo done
exit 1
