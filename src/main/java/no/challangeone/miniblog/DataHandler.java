package no.challangeone.miniblog;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class DataHandler {

    public final int millisecondsPerDay = 1000 * 60 * 60 * 24;

    public Timestamp timestampNow(){
        Long now = System.currentTimeMillis();
        now -= now % 1000; //Round off to closest second
        return new Timestamp(now);
    }

    public Timestamp addDays(Timestamp timestamp, int days){
        return new Timestamp(timestamp.getTime() + days*millisecondsPerDay);
    }
}
