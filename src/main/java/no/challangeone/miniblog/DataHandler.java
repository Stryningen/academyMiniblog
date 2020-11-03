package no.challangeone.miniblog;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class DataHandler {

    public Timestamp timestampNow(){
        Long now = System.currentTimeMillis();
        now -= now % 1000; //Round off to closest second
        return new Timestamp(now);
    }
}
