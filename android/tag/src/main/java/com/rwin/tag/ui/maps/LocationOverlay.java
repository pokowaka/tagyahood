package com.rwin.tag.ui.maps;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.safecanvas.ISafeCanvas;
import org.osmdroid.views.safecanvas.SafePaint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;

/**
 * @author erwinj
 * 
 */
public class LocationOverlay extends MyLocationNewOverlay {

    private static final String TAG = "com.ctenophore.ui.LocationOverlay";

    // Size of the circle we draw..
    public static final float MY_RADIUS = 300f;

    private SafePaint dot = new SafePaint();
    private int pos = 0;
    private long lastUpdate = 0;
    private SafePaint circlePaint = new SafePaint();

    public LocationOverlay(Context context, MapView view, IMyLocationProvider lp) {
        super(context, lp, view);

        // http://colorschemedesigner.com/#3L61Tw0w0w0w0
        dot.setColor(Color.parseColor("#1240AB"));
        circlePaint.setColor(Color.parseColor("#2A4480"));
        circlePaint.setAlpha(100);
    }

    @Override
    protected void drawMyLocation(ISafeCanvas canvas, MapView map,
            Location lastFix) {
        Point point = new Point();
        IGeoPoint geoPoint = new GeoPoint(lastFix);
        Projection projection = map.getProjection();
        projection.toMapPixels(geoPoint, point);

        int radius = (int) map.getProjection().metersToEquatorPixels(
                this.MY_RADIUS);

        // The players white circle.
        canvas.drawCircle(point.x, point.y, radius, circlePaint);
        super.drawMyLocation(canvas, map, lastFix);
    }

    /**
     * Returns true if the given pixel falls within the players radius
     * 
     * @param pt
     * @return
     */
    private boolean inCircle(Point pt, MapView mapView) {
        Point center = mapView.getProjection().toPixels(getMyLocation(), null);
        int radius = 0;
        if (MY_RADIUS > 0.0D)
            radius = (int) mapView.getProjection().metersToEquatorPixels(
                    (float) MY_RADIUS);
        int dx = center.x - pt.x;
        int dy = center.y - pt.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= (double) radius;
    }
}
