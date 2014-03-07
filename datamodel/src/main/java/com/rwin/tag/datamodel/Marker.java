package com.rwin.tag.datamodel;

import com.rwin.tag.util.Util;

public class Marker {

    public static final int MAX_ZOOM = 18;

    public Marker() {
    }

    public Marker(int x, int y, ArtPiece art, String userid) {
        super();
        this.x = x;
        this.y = y;
        this.owner = userid;
        this.art = art;
        created = System.currentTimeMillis();
    }

    public int x;
    public int y;
    public ArtPiece art;
    public long created;
    public String owner;

    @Override
    public String toString() {
        return Util.toJsonString(this);
    }

    public static Marker parse(String content) {
        return Util.parse(content, Marker.class);
    }
}
