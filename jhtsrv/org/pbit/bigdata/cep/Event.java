package org.pbit.bigdata.cep;

public class Event {

    private String s;
    private String ts;
    protected String ip;

    public final String getIp() {
	return ip;
    }

    public final String getS() {
	return s;
    }

    public final String getTs() {
	return ts;
    }

    public Event(String ip, String s, String ts) {
	this.ip = ip;
	this.s = s;
	this.ts = ts;
    }
}