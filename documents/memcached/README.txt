Memcached is a distributed cache solution. 

To install on Ubuntu
sudo apt-get install memcached
by default the memcached server will listen to 127.0.0.1:11211

Bime has support of memcached at web-controller layter through the MemcachedManager
By default the MemcachedManager is turned off (see MemcachedManager.isTurnedOn)
To turn it on at start up, change the bime.properties, set bime.cached=true
To turn it on in runtime, use Restful url
http://.../support/cache/turnOn.html (POST)
To turn it off in runtime, use Restful url
http://.../support/cache/turnOff.html (POST)
To list the connected memcache servers
http://.../support/cache/list.html (GET)
To add memcache server to MemcacheManager
http://.../support/cache/1.2.3.4/11211 (POST)
to remove memcache server to MemcachedManager
http://.../support/cache/1.2.3.4/11211 (DELETE)
