package com.rwin.tag.ui.maps;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * A VisibleOverlay only draws itself when it is visible. Subclass this thing
 * and implement the abstract methods and you are good to go.
 * 
 * @author erwinj
 * 
 */
public abstract class VisibleOverlay extends Overlay {

    protected MapView mapView;
    protected boolean firstDraw = true;
    private boolean wasVisible;

    public VisibleOverlay(Context context) {
        super(context);
    }

    protected Resources getResources() {
        return this.mapView.getResources();
    }

    /**
     * Checks whether or not the given point is currently visible. This can be
     * used to prevent drawing things that are not visible.
     * 
     * @param point
     * @return True if the given point is currently visible in the mapview.
     */
    protected boolean isPointVisible(IGeoPoint point) {
        if (mapView.getProjection() == null)
            return false;

        Rect currentMapBoundsRect = new Rect();
        Point currentDevicePosition = new Point();

        mapView.getProjection().toPixels(point, currentDevicePosition);
        mapView.getDrawingRect(currentMapBoundsRect);

        return currentMapBoundsRect.contains(currentDevicePosition.x,
                currentDevicePosition.y);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        IGeoPoint center = mapView.getMapCenter();
        boolean visible = isVisible();
        if (visible) {
            this.doDraw(canvas, mapView, shadow);
        } else {
            if (wasVisible)
                hide();
        }
        wasVisible = visible;
    }

    /**
     * Subclasses should implement this, draw on the canvas and make it work.
     * 
     * @param canvas
     * @param mapView2
     * @param shadow
     */
    public abstract void doDraw(Canvas canvas, MapView mapView2, boolean shadow);

    /**
     * Subclasses can override this to determine whether or not their layer is
     * visible.
     * 
     * @return
     */
    public abstract boolean isVisible();

    /**
     * Subclasses can override this if they need to do something when they are
     * no longer painted.
     */
    public void hide() {
    }

}