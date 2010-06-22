from amqplib import client_0_8 as amqp
import sys
conn=amqp.Connection(host="192.168.8.128:5672", userid="guest", password="guest", virtual_host="/", insist=False)
chan=conn.channel()
msg=amqp.Message(sys.argv[1])
msg.properties["delivery_mode"]=1
chan.basic_publish(msg,exchange="haproxy",routing_key="configuration")
chan.close()
conn.close()

