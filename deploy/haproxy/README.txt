haproxy is used here as load balancer

In a scaled environment, for example, Amazon Web Service
we might have
1 EC2 has haproxy and hornetq (queue server)
1 EC2 to deploy BimeController and its MySql database
N EC2 to hold the Bime application
N ESB to hold mysql data file.


						  		            Bime Controller EC2 instance 192.168.2.1
								            Bime EC2 A instance 192.168.2.2     ---> mysql A
								            Bime EC2 B instance 192.168.2.3     ---> mysql B
  ---->  haproxy (public ip) ----------> 	Bime EC2 C ...
    					            		Bime EC2 D ...
    					            		Bime EC2 E ...
    					            		Bime EC2 F ...
    							            ....
    	
As a Saas service provider, assume we will have 100 clients, then for each client, they
will have their own domain name "clientN.bime.com",and their own database too.
We know that one Tomcat server might be able to handle at most 10 clients (assume from every
client, average 20 cuoncurrent users)
So, at our Haproxy, we need to configure

requests to www.bime.com go to EC2 instance (bime controller)
requests to client1.bime.com/client2.bime.com/../client10.bime.com all go to EC2 instance A
requests to client11.bime.com/client12.bime.com/.../client20 all go to EC2 instance B
....

