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
#