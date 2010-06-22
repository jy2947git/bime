package com.focaplo.myfuse.logging;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import scribe.thrift.LogEntry;
import scribe.thrift.scribe.Client;


public class SimpleScribeAppender extends AppenderSkeleton {
	private List<LogEntry> logEntries;

    private String hostname;
    private String scribe_host = "127.0.0.1";
    private int scribe_port = 1463;
    private String scribe_category = "scribe";

    private Client client;
    private TFramedTransport transport;
    
    public void configureScribe() {
        try {
            synchronized(this) {
                if (hostname == null) {
                    try {
                        hostname = InetAddress.getLocalHost().getCanonicalHostName();
                    } catch (UnknownHostException e) {
                        // can't get hostname
                    }
                }

                // Thrift boilerplate code
                logEntries = new ArrayList<LogEntry>(1);
                TSocket sock = new TSocket(new Socket(scribe_host, scribe_port));
                transport = new TFramedTransport(sock);
                TBinaryProtocol protocol = new TBinaryProtocol(transport, false, false);
                client = new Client(protocol, protocol);
            }
        } catch (TTransportException e) {
            System.err.println(e);
        } catch (UnknownHostException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
	@Override
	protected void append(LoggingEvent loggingEvent) {
		synchronized(this) {

            connect();

            try {
                String message = String.format("%s %s", hostname, layout.format(loggingEvent));
                LogEntry entry = new LogEntry(scribe_category, message);

                logEntries.add(entry);
                client.Log(logEntries);
            } catch (TTransportException e) {
                transport.close();
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                logEntries.clear();
            }
        }

	}
	public void connect() {
        if (transport != null && transport.isOpen())
            return;

        if (transport != null && transport.isOpen() == false)
        {
            transport.close();

        }
        configureScribe();
    }
	@Override
	public void close() {
		if (transport != null && transport.isOpen()) {
            transport.close();
        }

	}

	@Override
	public boolean requiresLayout() {

		return true;
	}

	public List<LogEntry> getLogEntries() {
		return logEntries;
	}

	public void setLogEntries(List<LogEntry> logEntries) {
		this.logEntries = logEntries;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getScribe_host() {
		return scribe_host;
	}

	public void setScribe_host(String scribeHost) {
		scribe_host = scribeHost;
	}

	public int getScribe_port() {
		return scribe_port;
	}

	public void setScribe_port(int scribePort) {
		scribe_port = scribePort;
	}

	public String getScribe_category() {
		return scribe_category;
	}

	public void setScribe_category(String scribeCategory) {
		scribe_category = scribeCategory;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public TFramedTransport getTransport() {
		return transport;
	}

	public void setTransport(TFramedTransport transport) {
		this.transport = transport;
	}

}
