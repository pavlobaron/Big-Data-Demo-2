package org.pbit.bigdata.replication;

import com.rackspacecloud.client.cloudfiles.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Cloud implements MapReduce.Out {

    protected static String suffix = "-VOTES";

    FilesClient client;
    String container;

    public Cloud(String c) {
	client = new FilesClient("test:tester", "testing");

	try {
	    client.login();

	    System.out.println("cloud login as: " + client.getAccount() +
			       ", " + client.isLoggedin());

	    container = genContainer(c);
	    if (!client.containerExists(container)) client.createContainer(container);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void store(String name, String record) {
	try {
	    client.storeObject(container,
			    record.getBytes(),
			    "text/plain",
			    name,
			   new HashMap<String, String>());
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    protected String genContainer(String c) throws Exception {
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	return c +
	    "-" +
	    sdf.format(cal.getTime()) +
	    "-" +
	    UUID.randomUUID().toString() +
	    suffix;
    }
}
