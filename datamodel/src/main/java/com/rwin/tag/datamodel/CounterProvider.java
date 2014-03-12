package com.rwin.tag.datamodel;

import java.util.concurrent.atomic.AtomicLong;

public class CounterProvider implements IdProvider {

    AtomicLong id = new AtomicLong();

    @Override
    public long getNextId() {
        return id.incrementAndGet();
    }

}
