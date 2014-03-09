package com.rwin.tag.datamodel;

import java.util.UUID;

import quicktime.std.image.GECompressorInfo;

import com.rwin.tag.util.OpenTile;
import com.rwin.tag.util.Util;

public class Marker {

    public static final int MAX_ZOOM = 18;

    public static Marker parse(String content) {
        return Util.parse(content, Marker.class);
    }

    public ArtPiece art;
    public long created;
    public double lat;
    public double lon;
    public String owner;
    
    public Marker() {
    }

    public Marker(ArtPiece art, String name) {
        this.owner = name;
        this.art = art;
        created = System.currentTimeMillis();
    }

    public Marker(double lat, double lon, ArtPiece art, String userid) {
        this.lat = lat;
        this.lon = lon;
        this.owner = userid;
        this.art = art;
        created = System.currentTimeMillis();
    }

    public int getX(int zoom) {
        return OpenTile.getXTile(this.lon, zoom);
    }

    public int getY(int zoom) {
        return OpenTile.getYTile(this.lat, zoom);
    }

    public void setX(int zoom, int x) {
        this.lon = OpenTile.tile2Lon(x + 1, zoom);
    }

    public void setY(int zoom, int y) {
        this.lat = OpenTile.tile2Lat(y, zoom);
    }

    @Override
    public String toString() {
        return Util.toJsonString(this);
    }

}
