package com.rwin.tag.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.BoundingBoxE6;

import android.location.Location;

import com.rwin.tag.datamodel.ArtPiece;
import com.rwin.tag.datamodel.Marker;

public class DataModel {

    private static final String TAG = "com.rwin.tag.data.DataModel";

    private static final double E6 = 1000000;

    // Invariant, this can never be null..
    private Collection<Marker> markers = new ArrayList<Marker>();
    private ConcurrentHashMap<String, ArtPiece> art = new ConcurrentHashMap<String, ArtPiece>();

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private BoundingBoxE6 markerBounds = new BoundingBoxE6(0, 0, 0, 0);

    public Collection<Marker> getMarkers() {
        return markers;
    }

    public BoundingBoxE6 getMarkerBounds() {
        return markerBounds;
    }

    public void setMarkers(Collection<Marker> markers) {

        if (markers == null)
            return;

        Collection<Marker> oldValue = this.markers;
        this.markers = markers;

        int minLat = Integer.MAX_VALUE;
        int minLon = Integer.MAX_VALUE;
        int maxLat = Integer.MIN_VALUE;
        int maxLon = Integer.MIN_VALUE;
        for (Marker m : markers) {
            int latitudeE6 = (int) (m.lat * E6);
            int longitudeE6 = (int) (m.lon * E6);

            minLat = Math.min(minLat, latitudeE6);
            minLon = Math.min(minLon, longitudeE6);
            maxLat = Math.max(maxLat, latitudeE6);
            maxLon = Math.max(maxLon, longitudeE6);
        }

        markerBounds = new BoundingBoxE6(maxLat, maxLon, minLat, minLon);
        pcs.firePropertyChange("markers", oldValue, this.markers);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    public ArtPiece getArt(String id) {
        return art.get(id);
    }

    public void addArt(ArtPiece piece) {
        if (piece != null && piece.id != null)
            art.put(piece.id.toString(), piece);
    }

    public Marker getClosestMarker(IGeoPoint gp) {
        float dist = Float.MAX_VALUE;
        Marker closest = null;
        double lat = gp.getLatitude();
        double lon = gp.getLongitude();
        float result[] = new float[1];
        for (Marker m : markers) {
            Location.distanceBetween(lat, lon, m.lat, m.lon, result);
            if (result[0] < dist) {
                closest = m;
                dist = result[0];
            }
        }
        return closest;
    }
}
