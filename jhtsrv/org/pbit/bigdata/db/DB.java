package org.pbit.bigdata.db;

import com.basho.riak.client.*;

public class DB {

    protected static String BUCKET = "VOTES";

    RiakClient riak;

    public DB() {
	riak = new RiakClient("http://localhost:8098/riak");
    }

    public void store(String ip, String s, String ts) {
	String key = (ip + "_" + ts).replace(' ', '-').replace(':', '-');
	riak.store(new RiakObject(riak, BUCKET, key,
				  (ip + "," +
				   ts + "," +
				   s).getBytes()));
    }
}