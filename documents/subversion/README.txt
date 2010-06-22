#
# Subversion server for source control
#

sudo apt-get -y install subversion libapache2-svn
svnadmin create ~/svn-repository
sudo chown -R www-data:www-data ~/svn-repository
sudo htpasswd -c /etc/subversion/passwd jy2947
#sudo htpasswd /etc/subversion/password user_name
#
# need to configure Apache2
#