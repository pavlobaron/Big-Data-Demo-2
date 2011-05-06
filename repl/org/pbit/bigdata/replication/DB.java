package org.pbit.bigdata.replication;

import com.basho.riak.client.*;
import com.basho.riak.client.response.*;
import com.basho.riak.client.request.*;

import java.util.Collection;

public class DB implements MapReduce.In {
    
    protected static String BUCKET = "VOTES";

    RiakClient riak;

    public DB() {
	riak = new RiakClient("http://localhost:8098/riak");
    }

    public Collection<String> getKeys() {
	BucketResponse r = riak.listBucket(BUCKET);
	if (r.isSuccess()) {
	    RiakBucketInfo info = r.getBucketInfo();

	    return info.getKeys();
	}

	return null;
    }

    public String getValue(String key) {
	FetchResponse rr = riak.fetch(BUCKET, key);
	String ret = "";
	if (rr.isSuccess()) {
	    RiakObject o = rr.getObject();
	    ret = (String)o.getValue();
	}

	return ret;
    }

    public void remove(String key) {
	riak.delete(BUCKET, key, RequestMeta.writeParams(1, 0));
    }
}
