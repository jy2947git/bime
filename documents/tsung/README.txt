#
#Tsung (http://tsung.erlang-projects.org/) is used for load testing.
#
#Tsung needs Erlang installed on the Linux system.
#

cd ~
sudo apt-get -y install erlang
sudo apt-get -y install erlang-nox
sudo apt-get -y install gnuplot-nox
sudo apt-get -y install libtemplate-perl libhtml-template-perl libhtml-template-expr-perl
wget http://tsung.erlang-projects.org/dist/ubuntu/tsung_1.3.1-1_all.deb
sudo dpkg -i tsung_1.3.1-1_all.deb
#
# the configure Apache2 to enable the tsung reports site
# a2ebsute tsung
#
# also configure haproxy to set up Tsung ACL
# 		acl acl_tsung hdr_dom(host) -i tsung.bimelab.com
#		use_backend tsung_server if acl_tsung
# backend tsung_server
#       mode http
#        server server1 127.0.0.1:6080