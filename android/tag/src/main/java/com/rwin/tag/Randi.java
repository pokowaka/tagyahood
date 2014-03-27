package com.rwin.tag;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.GpsLocationProvider;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.tileprovider.tilesource.MapboxTileLayer;
import com.mapbox.mapboxsdk.views.MapController;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.util.TilesLoadedListener;
import com.rwin.tag.data.DataController;

public class Randi extends Activity {
    private SimpleLocationTracker mTracker;

    private static final String TAG = "com.rwin.randy.Randi";

    private MapController mapController;
    private LatLng startingPoint = new LatLng(51f, 0f);
    private MapView mv;
    private UserLocationOverlay myLocationOverlay;
    private Paint paint;
    private String blackandwhite = "examples.map-20v6611k";
    private PathOverlay equator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(com.rwin.tag.R.layout.activity_randi);

        mv = (MapView) findViewById(R.id.mapviewr);
        mv.setTileSource(new MapboxTileLayer(blackandwhite));
        mapController = mv.getController();
        //addLocationOverlay();
        mv.setVisibility(View.VISIBLE);
        mv.setCenter(new LatLng(37.766372, -122.405677));
        mv.setZoom(15);
    }

    private void addLocationOverlay() {
        // Adds an icon that shows location
        myLocationOverlay = new UserLocationOverlay(new GpsLocationProvider(
                this), mv);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.setDrawAccuracyEnabled(true);
        myLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                mv.setCenter(myLocationOverlay.getMyLocation());
            }
        });
        mv.getOverlays().add(myLocationOverlay);
    }

    public LatLng getMapCenter() {
        return mv.getCenter();
    }

    public void setMapCenter(ILatLng center) {
        mv.setCenter(center);
    }
}
