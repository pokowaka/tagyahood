package com.rwin.tag.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class MarkerPolygonTest extends MarkerPolygon {

    public MarkerPolygonTest() {
        super(new Crew());
    }

    MarkerPolygon randomPoly(int minPts, int maxPts, int minRad, int maxRad) {

        Random generator = new Random();
        MarkerPolygon mp = new MarkerPolygon(c);
        // Set points using a min and max range
        int numPoints = (int) Math.floor(generator.nextDouble()
                * (maxPts - minPts))
                + minPts;

        int[] yPolyPoints = new int[numPoints];
        int[] xPolyPoints = new int[numPoints];

        // Set the radius using min and max range as well
        int radius = (int) Math.floor(generator.nextDouble()
                * (maxRad - minRad))
                + minRad;

        double crAng = 0,

        // Angle between each point
        angDiff = Math.toRadians(360.0 / numPoints),

        // Arbitrary radJitter range; notice I subtract each by the jitter / 2.0
        // to get a +/- jitter with this range
        radJitter = radius / 3.0, angJitter = angDiff * .9;

        for (int i = 0; i < numPoints; i++) {
            double tRadius = radius
                    + (generator.nextDouble() * radJitter - radJitter / 2.0);
            double tAng = crAng
                    + (generator.nextDouble() * angJitter - angJitter / 2.0);
            double nx = (Math.sin(tAng) * tRadius);
            double ny = (Math.cos(tAng) * tRadius);

            crAng += angDiff;
            Marker m = new Marker(ny, nx);
            mp.polygon.add(m);
        }
        return mp;
    }

    Crew c = new Crew();

    @Test
    public void testInside() {
        List<Marker> asList = Arrays.asList(new Marker(0, 0), new Marker(0, 2),
                new Marker(2, 2), new Marker(2, 0), new Marker(0, 0));
        MarkerPolygon poly = new MarkerPolygon(c, asList);
        assertTrue(poly.inside(new Marker(1, 1)));
        assertTrue(poly.inside(new Marker(2, 2)));
        assertFalse(poly.inside(new Marker(1, 3)));
        assertFalse(poly.inside(new Marker(3, 3)));
    }

    @Test
    public void testSplit() {
        // Note this is a looped polygon..
        List<Marker> asList = Arrays.asList(new Marker(0, 0), new Marker(0, 2),
                new Marker(2, 2), new Marker(2, 0), new Marker(0, 0));
        MarkerPolygon poly = new MarkerPolygon(c, asList);
        MarkerPolygon poly2 = poly.splitBefore(new Marker(2, 2));

        assertFalse(poly.isClosed());
        assertEquals(2, poly.polygon.size());
        assertEquals(new Marker(2, 2), poly2.first());
        assertEquals(2, poly2.polygon.size());

        List<Marker> asList2 = Arrays.asList(new Marker(0, 0),
                new Marker(0, 2), new Marker(2, 2), new Marker(2, 0));
        MarkerPolygon poly3 = new MarkerPolygon(c, asList2);
        MarkerPolygon poly4 = poly3.splitBefore(new Marker(2, 0));
        assertEquals(1, poly4.polygon.size());

        poly3 = new MarkerPolygon(c, asList2);
        poly4 = poly3.splitBefore(new Marker(0, 0));
        assertEquals(4, poly4.polygon.size());
        assertEquals(0, poly3.polygon.size());

        poly3 = new MarkerPolygon(c, asList2);
        poly4 = poly3.splitAfter(new Marker(2, 2));
        assertEquals(1, poly4.polygon.size());
        assertEquals(3, poly3.polygon.size());

    }

    @Test
    public void testCombine() {
        Crew c = new Crew();
        Marker m1 = new Marker(0, 0);
        Marker m2 = new Marker(0, 2);
        MarkerPolygon p = MarkerPolygon.combine(c, m1, m2);
        assertEquals(2, p.polygon.size());

        Marker m3 = new Marker(2, 2);
        MarkerPolygon p3 = MarkerPolygon.combine(c, m2, m3);

        // Well, m3 should have ended up in the same polygon, since m3 is glued
        // at the end of m1,m2,m3
        assertEquals(p, p3);

        Marker m4 = new Marker(1, 1);
        Marker m5 = new Marker(2, 3);
        Marker m6 = new Marker(5, 5);
        MarkerPolygon p4 = new MarkerPolygon(c, Arrays.asList(m4, m5, m6));

        p = MarkerPolygon.combine(c, m2, m5);
        assertEquals(m1, p.first());
        assertEquals(m6, p.last());

        Marker[] markers = new Marker[] { m1, m2, m5, m6 };
        int i = 0;
        for (Marker m : p) {
            assertEquals(markers[i++], m);
        }
    }

    @Test
    public void testArea() {
        List<Marker> asList = Arrays.asList(new Marker(0, 0), new Marker(0, 2),
                new Marker(2, 2), new Marker(2, 0), new Marker(0, 0));
        MarkerPolygon poly = new MarkerPolygon(c, asList);

        assertEquals(4d, poly.getArea(), 0.1d);
    }

    @Test
    public void testAdd() {
        List<Marker> asList = Arrays.asList(new Marker(0, 0), new Marker(0, 2),
                new Marker(2, 2), new Marker(2, 0), new Marker(2, 3));
        MarkerPolygon poly = new MarkerPolygon(c, asList);
        poly.insertAfter(null, new Marker(1, 1));
        assertEquals(6, poly.polygon.size());
        assertEquals(new Marker(1, 1), poly.polygon.peekFirst());
        poly.insertAfter(new Marker(2, 3), new Marker(1, 1));
        assertEquals(7, poly.polygon.size());
        assertEquals(new Marker(1, 1), poly.polygon.peekLast());
        assertTrue(poly.isClosed());

        // Break the loop
        poly.insertAfter(null, new Marker(6, 7));
        assertEquals(7, poly.polygon.size());
        assertFalse(poly.isClosed());

    }
}
