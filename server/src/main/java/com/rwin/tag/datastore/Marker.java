package com.rwin.tag.datastore;

public class Marker {

    public Marker() {
    }

    public Marker(int x, int y, long userid) {
        super();
        this.x = x;
        this.y = y;
        this.userid = userid;
        created = System.currentTimeMillis();
    }

    public int x;
    public int y;
    public ArtPiece art;
    public long created;
    public long userid;

}
