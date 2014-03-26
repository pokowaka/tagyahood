package com.rwin.tag.ui.maps;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.location.Location;
import android.view.MotionEvent;

import com.rwin.tag.data.DataController;
import com.rwin.tag.data.DataModel;
import com.rwin.tag.datamodel.Marker;

public class ArtOverlay extends VisibleOverlay {

    public interface MarkerSelectionListener {
        void selected(Marker m, MapView mapView);
    }

    private static final String TAG = "com.rwin.tag.ui.maps.ArtOverlay";
    boolean changed = false;
    private DataModel model;
    private Paint paint = new Paint();
    private MarkerSelectionListener selectionListener;

    public ArtOverlay(Context context) {
        super(context);
        this.model = DataController.getInstance().getModel();

        paint.setTextSize(20);
        paint.setStrokeWidth(3);
        paint.setAlpha(0x80);
        paint.setStyle(Style.FILL_AND_STROKE);
        model.addPropertyChangeListener("markers",
                new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent event) {
                        // New data! Please update the view..
                        changed = true;
                        if (ArtOverlay.this.mapView != null)
                            ArtOverlay.this.mapView.postInvalidate();
                    }
                });
    }

    @Override
    public void doDraw(final Canvas canvas, MapView mapView2, boolean shadow) {
        Projection projection = this.mapView.getProjection();

        changed = false;
        Point p0 = new Point();

        for (Marker m : getVisibleMarkers()) {
            IGeoPoint nw = new GeoPoint(m.lat, m.lon);
            projection.toPixels(nw, p0);

            // paint.setColor(m.owner);
            canvas.drawCircle(p0.x, p0.y, 10, paint);
        }
    }

    private boolean intersects(BoundingBoxE6 fst, BoundingBoxE6 snd) {
        // If any of the sides from A are outside of B
        if (fst.getLatNorthE6() < snd.getLatSouthE6()) {
            return false;
        }

        if (snd.getLatNorthE6() < fst.getLatSouthE6()) {
            return false;
        }

        if (fst.getLonEastE6() < snd.getLonWestE6()) {
            return false;
        }

        if (snd.getLonEastE6() < fst.getLonWestE6()) {
            return false;
        }

        return true;
    }

    private Collection<Marker> getVisibleMarkers() {
        ArrayList<Marker> visible = new ArrayList<Marker>();
        BoundingBoxE6 bounds = ((ArtMapView) this.mapView).getBoundingBox();
        for (Marker m : model.getMarkers()) {
            if (bounds.contains(new GeoPoint(m.lat, m.lon))) {
                visible.add(m);
            }
        }
        return visible;
    }

    @Override
    public boolean isVisible() {
        if (this.mapView == null)
            return false;
        
        // Visible if the bounding coordinate bounding box contains
        boolean visible = intersects(((ArtMapView) this.mapView).getBoundingBox(),
                model.getMarkerBounds());
        return visible;
    }

    public boolean needsUpdate(IGeoPoint mapCenter) {
        return changed;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        if (selectionListener == null)
            return false;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Projection p = mapView.getProjection();
            IGeoPoint gp = p.fromPixels(event.getX(), event.getY());
            Marker m = DataController.getInstance().getModel()
                    .getClosestMarker(gp);

            if (m == null)
                return false;
            float[] results = new float[1];
            // Now check if this falls within click range... (say 5m)
            Location.distanceBetween(gp.getLatitude(), gp.getLongitude(),
                    m.lat, m.lon, results);
            if (results[0] < 5) {
                selectionListener.selected(m, mapView);
                return true;
            }

        }
        return false;
    }

    public void setSelectionListener(MarkerSelectionListener listener) {
        this.selectionListener = listener;
    }
}
