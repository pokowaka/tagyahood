package com.rwin.tag.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rwin.tag.util.OpenTile;
import com.rwin.tag.util.Util;

public class Marker {

    public static IdProvider idProvider = new CounterProvider();

    public static final int MAX_ZOOM = 18;
    private static final long SEVEN_DAYS = 1 * 1000 * 60 * 60 * 24 * 7;

    public static Marker parse(String content) {
        return Util.parse(content, Marker.class);
    }

    public ArtPiece art;
    public long created;
    public double lat;
    public double lon;
    public User owner;
    // Uniqueid, generated by the server. (Using snowflake twitter)
    public long marker_id = idProvider.getNextId();

    @JsonIgnore
    public MarkerPolygon polygon = new MarkerPolygon();

    public Marker() {
        this.polygon.add(this);
    }

    public Marker(double lat, double lon, ArtPiece art, User user) {
        this.lat = lat;
        this.lon = lon;
        this.owner = user;
        this.art = art;
        created = System.currentTimeMillis();
        this.polygon.add(this);
    }

    public Marker(double lat, double lon) {
        this(lat, lon, null, null);
    }

    public Marker(User u) {
        this.owner = u;
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

    public int calculateFame(long from, long until) {
        long start = Math.max(from, created);
        long ms = Math.max(Math.min(until - start, 0), SEVEN_DAYS);
        return (int) (art.famePoints() * (SEVEN_DAYS / ms));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Marker other = (Marker) obj;
        if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
            return false;
        if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon))
            return false;
        return true;
    }
}
