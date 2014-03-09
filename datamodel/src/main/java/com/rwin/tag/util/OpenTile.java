package com.rwin.tag.util;

import java.awt.Rectangle;

public class OpenTile {
    public double north;
    public double south;
    public double east;
    public double west;

    @Override
    public String toString() {
        return "OpenTile [north=" + north + ", south=" + south + ", east="
                + east + ", west=" + west + "]";
    }

    public static OpenTile tile2boundingBox(final int x, final int y,
            final int zoom) {
        OpenTile bb = new OpenTile();
        bb.north = tile2Lat(y, zoom);
        bb.south = tile2Lat(y + 1, zoom);
        bb.west = tile2Lon(x, zoom);
        bb.east = tile2Lon(x + 1, zoom);
        return bb;
    }

    public static OpenTile tile2BB(final int x, final int y, final int zoom,
            final int outzoom) {
        // TODO(ErwinJ): There likely is a faster & safer way..
        OpenTile bb = new OpenTile();
        bb.north = getYTile(tile2Lat(y, zoom), outzoom);
        bb.south = getYTile(tile2Lat(y + 1, zoom), outzoom);
        bb.west = getXTile(tile2Lon(x, zoom), outzoom);
        bb.east = getXTile(tile2Lon(x + 1, zoom), outzoom);
        return bb;
    }

    public static double tile2Lon(int x, int z) {
        return x / Math.pow(2.0, z) * 360.0 - 180;
    }

    public static double tile2Lat(int y, int z) {
        double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }

    /**
     * Converts a gps coordinate & zoom to an actual URL for an openmap tile.
     * 
     * @param lat
     * @param lon
     * @param zoom
     * @return
     */
    public static String getTileNumber(final double lat, final double lon,
            final int zoom) {
        int xtile = getXTile(lon, zoom);
        int ytile = getYTile(lat, zoom);
        return ("" + zoom + "/" + xtile + "/" + ytile);
    }

    public static int getYTile(final double lat, final int zoom) {
        int ytile = (int) Math
                .floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1
                        / Math.cos(Math.toRadians(lat)))
                        / Math.PI)
                        / 2 * (1 << zoom));
        if (ytile < 0)
            ytile = 0;
        if (ytile >= (1 << zoom))
            ytile = ((1 << zoom) - 1);
        return ytile;
    }

    public static int getXTile(final double lon, final int zoom) {
        int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
        if (xtile < 0)
            xtile = 0;
        if (xtile >= (1 << zoom))
            xtile = ((1 << zoom) - 1);
        return xtile;
    }

}