package com.rwin.tag.datamodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.rwin.tag.util.Util;

/**
 * TODO Perf sux
 * 
 * @author erwinj
 * 
 */
public class MarkerPolygon implements Iterable<Marker> {

    private class MarkerPolygonComparator implements Comparator<Marker> {

        Marker center;

        public MarkerPolygonComparator(Marker center) {
            this.center = center;
        }

        @Override
        public int compare(Marker a, Marker b) {
            if (a.lon == b.lon && a.lat == b.lat)
                return 0;
            return less(a, b) ? -1 : 1;
        }

        boolean less(Marker a, Marker b) {
            if (a.lon - center.lon >= 0 && b.lon - center.lon < 0)
                return true;
            if (a.lon - center.lon < 0 && b.lon - center.lon >= 0)
                return false;
            if (a.lon - center.lon == 0 && b.lon - center.lon == 0) {
                if (a.lat - center.lat >= 0 || b.lat - center.lat >= 0)
                    return a.lat > b.lat;
                return b.lat > a.lat;
            }

            // compute the cross product of vectors (center -> a) x (center ->
            // b)
            double det = (a.lon - center.lon) * (b.lat - center.lat)
                    - (b.lon - center.lon) * (a.lat - center.lat);
            if (det < 0)
                return true;
            if (det > 0)
                return false;

            // points a and b are on the same line from the center
            // check which point is closer to the center
            double d1 = (a.lon - center.lon) * (a.lon - center.lon)
                    + (a.lat - center.lat) * (a.lat - center.lat);
            double d2 = (b.lon - center.lon) * (b.lon - center.lon)
                    + (b.lat - center.lat) * (b.lat - center.lat);
            return d1 > d2;
        }
    }

    public static IdProvider idProvider = new CounterProvider();

    public static MarkerPolygon combine(Crew c, Marker fst, Marker snd) {
        MarkerPolygon fstPoly = fst.polygon;
        MarkerPolygon sndPoly = snd.polygon;

        // Okay, now create polygons where the marker is at the beginning..
        MarkerPolygon headSnd = sndPoly.splitBefore(snd);
        fstPoly.splitAfter(fst);

        // We now return the polygin x-> ... -> fst -> snd ... -> z
        fstPoly.append(headSnd);

        return fstPoly;
    }

    public static MarkerPolygon parse(String content) {
        return Util.parse(content, MarkerPolygon.class);
    }

    public Crew crew = new Crew();

    public long id = idProvider.getNextId();
    public LinkedList<Marker> polygon = new LinkedList<Marker>();

    public MarkerPolygon() {
    }

    public MarkerPolygon(Crew crew) {
        this(crew, new LinkedList<Marker>());
    }

    public MarkerPolygon(Crew crew, List<Marker> polygon) {
        this.crew = crew;
        this.polygon = new LinkedList<Marker>();
        for (Marker m : polygon) {
            this.add(m);
        }
    }

    public void add(Marker m) {
        if (isClosed())
            this.polygon.removeLast();
        this.polygon.addLast(m);
        m.polygon = this;
    }

    public MarkerPolygon append(MarkerPolygon aMarkerPolygon) {
        if (this.isClosed()) {
            this.polygon.removeLast();
        }

        if (aMarkerPolygon.isClosed()) {
            aMarkerPolygon.polygon.removeFirst();
        }

        for (Marker m : aMarkerPolygon.polygon) {
            this.add(m);
        }
        aMarkerPolygon.clear();

        return this;
    }

    public void clear() {
        this.polygon.clear();
    }

    /**
     * Computes the centroid of this polygon.
     * 
     * @return
     */
    public synchronized Marker computeCentroid() {
        Marker centroid = new Marker(0, 0);
        if (polygon.size() < 2)
            return centroid;

        double signedArea = 0.0;
        double x0 = 0.0; // Current vertex X
        double y0 = 0.0; // Current vertex Y
        double x1 = 0.0; // Next vertex X
        double y1 = 0.0; // Next vertex Y
        double a = 0.0; // Partial signed area

        Marker p0 = polygon.iterator().next();
        Marker prev = p0;

        // For all vertices except last
        int i = 0;
        for (Marker p : polygon) {
            if (i++ == 0)
                continue;

            x0 = prev.lon;
            y0 = prev.lat;
            x1 = p.lon;
            y1 = p.lat;
            a = x0 * y1 - x1 * y0;
            signedArea += a;
            centroid.lon += (x0 + x1) * a;
            centroid.lat += (y0 + y1) * a;
            prev = p;
        }

        // Do last vertex
        x0 = prev.lon;
        y0 = prev.lat;
        x1 = p0.lon;
        y1 = p0.lat;
        a = x0 * y1 - x1 * y0;
        signedArea += a;
        centroid.lon += (x0 + x1) * a;
        centroid.lat += (y0 + y1) * a;

        signedArea *= 0.5;
        centroid.lon /= (6.0 * signedArea);
        centroid.lat /= (6.0 * signedArea);

        return centroid;
    }

    public Marker first() {
        return this.polygon.peekFirst();
    }

    public synchronized double getArea() {
        if (!isClosed())
            return 0d;

        double area = 0;
        Iterator<Marker> fst = polygon.iterator();
        Iterator<Marker> snd = polygon.iterator();
        snd.next();
        while (snd.hasNext()) {
            Marker mi = fst.next();
            Marker mi1 = snd.next();
            // sum = sum + x[i]*y[i+1] - y[i]*x[i+1];
            area = area + mi.lon * mi1.lat - mi.lat * mi1.lon;
        }
        return Math.abs(area / 2);

    }

    public synchronized void insertAfter(Marker after, Marker element) {
        int idx = this.polygon.indexOf(after);

        // if after == null, idx will be -1, hence we will add the beginning of
        // the list. (idx < size(), so we can safely call add)
        // Fix invariant if we add idx at 0, or at the end..
        if (isClosed() && (idx == -1 || idx == this.polygon.size() - 1)) {
            this.polygon.removeLast();
            if (idx == this.polygon.size()) {
                idx--;
            }
        }

        this.polygon.add(idx + 1, element);
        element.polygon = this;
    }

    public synchronized boolean inside(Marker m) {

        // // Check bounding box first.
        // boolean inBoundindBox = ((m.lat < northEast.lat)
        // && (m.lat > southWest.lat) && (m.lon < northEast.lon) && (m.lon >
        // southWest.lon));
        //
        // if (!inBoundindBox)
        // return false;

        int numverts = this.polygon.size();
        boolean yflag0, yflag1, inside_flag;
        double ty, tx;
        Marker vtx0, vtx1;

        tx = m.lon;
        ty = m.lat;

        vtx0 = this.polygon.get(numverts - 1);
        ;
        /* get test bit for above/below X axis */
        yflag0 = (vtx0.lat >= ty);
        Iterator<Marker> polyIter = this.polygon.iterator();
        vtx1 = polyIter.next();

        inside_flag = false;
        while (polyIter.hasNext()) {

            yflag1 = (vtx1.lat >= ty);
            /*
             * Check if endpoints straddle (are on opposite sides) of X axis
             * (i.e. the Y's differ); if so, +X ray could intersect this edge.
             * The old test also checked whether the endpoints are both to the
             * right or to the left of the test point. However, given the faster
             * intersection point computation used below, this test was found to
             * be a break-even proposition for most polygons and a loser for
             * triangles (where 50% or more of the edges which survive this test
             * will cross quadrants and so have to have the X intersection
             * computed anyway). I credit Joseph Samosky with inspiring me to
             * try dropping the "both left or both right" part of my code.
             */
            if (yflag0 != yflag1) {
                /*
                 * Check intersection of pgon segment with +X ray. Note if >=
                 * point's X; if so, the ray hits it. The division operation is
                 * avoided for the ">=" test by checking the sign of the first
                 * vertex wrto the test point; idea inspired by Joseph Samosky's
                 * and Mark Haigh-Hutchinson's different polygon inclusion
                 * tests.
                 */
                if (((vtx1.lat - ty) * (vtx0.lon - vtx1.lon) >= (vtx1.lon - tx)
                        * (vtx0.lat - vtx1.lat)) == yflag1) {
                    inside_flag = !inside_flag;
                }
            }

            /* Move to the next pair of vertices, retaining info as possible. */
            yflag0 = yflag1;
            vtx0 = vtx1;
            vtx1 = polyIter.next();
        }

        return (inside_flag);
    }

    public boolean isClosed() {
        if (polygon.size() < 2)
            return false;
        Marker fst = polygon.peekFirst();
        Marker snd = polygon.peekLast();

        return fst != null && fst.equals(snd);
    }

    public boolean isEmpty() {
        return this.polygon.isEmpty();
    }

    public Marker last() {
        return this.polygon.peekLast();
    }

    public void reverse() {
        Collections.reverse(this.polygon);
    }

    /**
     * Splits after the given marker. The original polygon will end with m.
     * 
     * So the following will hold. this.last() == m && !this.isClosed()
     * 
     * @param m
     * @return
     */
    public synchronized MarkerPolygon splitAfter(Marker m) {
        unloop();
        int iFst = polygon.indexOf(m);
        if (iFst == -1)
            return new MarkerPolygon(m.polygon.crew);

        List<Marker> sublist = polygon.subList(iFst + 1, polygon.size());
        MarkerPolygon split = new MarkerPolygon(this.crew, sublist);
        sublist.clear();
        return split;
    }

    /**
     * Splits the list before the m marker. The polygon returned will start with
     * m.
     * 
     * After this function the following will hold:
     * 
     * !this.isClosed() and return first() == m && !isClosed()
     * 
     * @param m
     * @return
     */
    public synchronized MarkerPolygon splitBefore(Marker m) {
        unloop();
        int iFst = polygon.indexOf(m);

        if (iFst < 0)
            return new MarkerPolygon(m.polygon.crew);

        List<Marker> sublist = polygon.subList(iFst, polygon.size());
        MarkerPolygon split = new MarkerPolygon(this.crew, sublist);
        sublist.clear();
        return split;
    }

    @Override
    public String toString() {
        return "MarkerList [crew=" + crew + ", polygon=" + polygon + ", id="
                + id + "]";
    }

    public void unloop() {
        if (isClosed()) {
            this.polygon.removeLast();
        }

    }

    @Override
    public Iterator<Marker> iterator() {
        return this.polygon.iterator();
    }

}
