package com.rwin.tag.datamodel;

public class GeoRect {

    double latN;
    double latS;
    double lonE;
    double lonW;

    public GeoRect(double latN, double latS, double lonE, double lonW) {
        super();
        this.latN = latN;
        this.latS = latS;
        this.lonE = lonE;
        this.lonW = lonW;
    }

    public GeoRect() {
        this.latN = Double.NEGATIVE_INFINITY;
        this.latS = Double.POSITIVE_INFINITY;
        this.lonE = Double.NEGATIVE_INFINITY;
        this.lonW = Double.POSITIVE_INFINITY;
    }

    public boolean intersects(GeoRect snd) {
        // If any of the sides from A are outside of B
        if (latN < snd.latS || snd.latN < latS || lonE < snd.lonW
                || snd.lonE < lonW) {
            return false;
        }

        return true;
    }

    public boolean contains(double lat, double lon) {
        return latS <= lat && lat <= latN && lonW <= lon && lon <= lonE;
    }

    public void extend(double lat, double lon) {
        latN = Math.max(latN, lat);
        latS = Math.min(latS, lat);
        lonE = Math.max(lonE, lon);
        lonW = Math.min(lonW, lon);
    }

    @Override
    public String toString() {
        return "GeoRect [latN=" + latN + ", latS=" + latS + ", lonE=" + lonE
                + ", lonW=" + lonW + "]";
    }

}
