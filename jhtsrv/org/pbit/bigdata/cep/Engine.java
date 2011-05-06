package org.pbit.bigdata.cep;

import com.espertech.esper.client.*;

public class Engine {

    EPServiceProvider ep;
    static String EPL =
    "select istream count(*) from Event(s='F').win:time_batch(20 sec) having count(*) > 100";

    public Engine() {
	Configuration config = new Configuration();
	config.addEventTypeAutoName("org.pbit.bigdata.cep");
	ep = EPServiceProviderManager.getDefaultProvider(config);
	EPStatement statement = ep.getEPAdministrator().createEPL(EPL);
	statement.addListener(new UpdateListener() {
		public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		    EventBean event = newEvents[0];
		    System.out.println("-------- Alert! ----- " +
				       event.get("count(*)") +
				       " messages with the status F");
		}
	    }
        );
    }

    public void send(Event e) {
	ep.getEPRuntime().sendEvent(e);
    }
}
