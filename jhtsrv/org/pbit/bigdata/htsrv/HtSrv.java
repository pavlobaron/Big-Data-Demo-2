package org.pbit.bigdata.htsrv;

import com.sun.net.httpserver.HttpHandler;
import java.util.Arrays;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import org.pbit.bigdata.cep.*;
import org.pbit.bigdata.db.*;

public class HtSrv implements HttpHandler {

    Random boolRandom;
    Random intRandom;
    Engine engine;
    DB db;

    public HtSrv(Random r1, Random r2, Engine engine, DB db) {
	boolRandom = r1;
	intRandom = r2;
	this.engine = engine;
	this.db = db;
    }
    
    public void handle(HttpExchange t) throws IOException {
	InputStream is = t.getRequestBody();
	byte[] ba = new byte[128];
	Arrays.fill(ba, (byte)0);
	is.read(ba);

	String response = "ok";
	t.sendResponseHeaders(200, response.length());
	OutputStream os = t.getResponseBody();
	os.write(response.getBytes());
	os.close();

	handleRequest(new String(ba));
    }

    protected void handleRequest(String request) {
	Event e = new Event(genIP(), genS(), genTS());

	System.out.println("incoming: from " + e.getIp() +
			   " -  " + e.getS() +
			   " @ " + e.getTs());

	engine.send(e);

	db.store(e.getIp(), e.getS(), e.getTs());
    }

    protected String genIP() {
	String s = "";
	for (int i = 0; i < 3; i++) s += "." + intRandom.nextInt(256);
	String ss = String.valueOf(intRandom.nextInt(255) + 1);
	
	return ss + s;
    }

    protected String genS() {
	Random random = new Random();
	if (boolRandom.nextBoolean()) return "S";
	else return "F";
    }

    protected String genTS() {
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	
	return sdf.format(cal.getTime());
    }

    public static void main(String [] args) {
	System.out.println("listening...");
        try {
	    HttpServer server = HttpServer.create(new InetSocketAddress(9876), 0);
	    server.createContext("/",
				 new HtSrv(new Random(),
					   new Random(),
					   new Engine(),
					   new DB()));
	    server.setExecutor(null);
	    server.start();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
