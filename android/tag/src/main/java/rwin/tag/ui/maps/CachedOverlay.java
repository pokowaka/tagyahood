package com.rwin.tag.ui.maps;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Subclass this if your paint logic is complex and it is faster to just bitblit
 * a cached bitmap.
 * 
 * Your subclass should override the needsUpdate() and return true if you need
 * to be repainted. Note, if your painting is super cheap you should not
 * subclass this thing.
 * 
 * @author erwinj
 */
public abstract class CachedOverlay extends VisibleOverlay {

    public CachedOverlay(Context context) {
        super(context);
    }

    private static final String TAG = "com.ctenophore.ui.overlays.CachedOverlay";

    protected Bitmap offscreen;
    boolean wasVisible = false;
    int lastZoomlevel;

    private IGeoPoint lastCenter;

    private void init(MapView map) {
        Rect bounds = new Rect();
        mapView.getDrawingRect(bounds);

        // Well, let's just pick RGB_565
        // http://www.curious-creature.org/2010/12/08/bitmap-quality-banding-and-dithering/
        offscreen = BitmapUtils.createBitmap(bounds.width(), bounds.height());
        firstDraw = true;
    }

    protected IGeoPoint getLastCenter() {
        return lastCenter;
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        long startTime = System.currentTimeMillis();
        // Make sure we initialize our offscreen buffer on first run as well
        // as view changes.
        if (offscreen == null || mapView.getWidth() != offscreen.getWidth()
                || mapView.getHeight() != offscreen.getHeight()) {
            firstDraw = true;
        }

        IGeoPoint center = mapView.getMapCenter();

        // We need to update on
        // 1. First time drawing.
        // 2. We moved
        // 3. We went from visible to invisible (or vice versa)
        // 4. Our zoomlevel changed.
        boolean visible = isVisible();
        if (firstDraw || !center.equals(lastCenter) || needsUpdate(center)
                || lastZoomlevel != mapView.getZoomLevel()
                || wasVisible != visible) {
            // Reset the cached bitmap..
            init(mapView);
            Canvas c = new Canvas(offscreen);

            if (!visible)
                hide();

            // Let the draw method update itself on the bitmap canvas..
            this.doDraw(c, mapView, shadow);
            firstDraw = false;
        } else {
            // Log.i(TAG, "Blitting from cache");
        }

        if (visible) {
            canvas.drawBitmap(offscreen, 0, 0, null);
        }

        lastZoomlevel = mapView.getZoomLevel();
        lastCenter = center;
        wasVisible = visible;
        // Log.d(TAG, "doDraw: time:" + (System.currentTimeMillis() -
        // startTime));

    };

    /**
     * Subclasses need to return true if there is new data that needs to be
     * processed.
     * 
     * You will always get a redraw wen the mapcenter has moved.
     * 
     * @return
     */
    public abstract boolean needsUpdate(IGeoPoint mapCenter);
}
