#
# Subversion server for source control
#

sudo apt-get -y install subversion libapache2-svn
svnadmin create ~/svn-repository
sudo chown -R www-data:www-data ~/svn-repository
sudo htpasswd -c /etc/subversion/passwd jy2947
#if add user to existing hapasswd file, remove the '-c'
#sudo htpasswd /etc/subversion/password user_name
#
# need to configure Apache2 to create svn virtual host in sites-avaliable
# then a2ensite svn
#
# need to configure Haproxy to set up ACL
# 		acl acl_svn hdr_dom(host) -i svn.bimelab.com
# 		use_backend svn_server if acl_svn
# backend svn_server
#        mode http
#        balance roundrobin
#        server server1 127.0.0.1:6080 check
#