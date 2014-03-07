package com.rwin.tag.datastore;

import java.util.concurrent.atomic.AtomicInteger;

public class Tag {

    public static final AtomicInteger counter = new AtomicInteger();

    /** Max zoom level supported by your map backend, usually this is 18. */
    public static final int MAX_ZOOM = 18;

    public int zoom;

    public Tag() {
    }

    public Tag(int x, int y, String id) {
        this.x = x;
        this.y = y;
        this.zoom = MAX_ZOOM;
        this.owner = id;
    }

    public int x;
    public int y;
    public String owner;

}
