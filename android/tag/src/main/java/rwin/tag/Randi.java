package com.rwin.tag;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;

import com.rwin.tag.ui.maps.LocationOverlay;
import com.rwin.tag.ui.maps.MapView;

public class Randi extends Activity {
    private Camera mCamera;
    private CameraPreview mPreview;
    private SimpleLocationTracker mTracker;
    private TagPoster tagger;
    private MapView mMapView;
    private CompassOverlay mCompassOverlay;
    private LocationOverlay mLocationOverlay;
    private MinimapOverlay mMinimapOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    // private RotationGestureOverlay mRotationGestureOverlay;
    private static final String TAG = "com.rwin.randy.Randi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = this;

        mMapView = new MapView(context);

        this.mTracker = new SimpleLocationTracker(this);
        mMapView.getController().setCenter(
                new GeoPoint(mTracker.getLastKnownLocation()));
        setContentView(mMapView);

        this.mCompassOverlay = new CompassOverlay(context,
                new InternalCompassOrientationProvider(context), mMapView);
        this.mCompassOverlay.enableCompass();
        this.mLocationOverlay = new LocationOverlay(context, mMapView, mTracker);
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();

        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        mMapView.getOverlays().add(this.mLocationOverlay);
        // mMapView.getOverlays().add(this.mCompassOverlay);

        mMapView.getController().setZoom(10);
        mLocationOverlay.enableMyLocation();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.randi, menu);
        return true;
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };
}
