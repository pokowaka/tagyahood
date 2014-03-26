package com.rwin.tag;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.rwin.tag.data.DataController;
import com.rwin.tag.data.DataModel;
import com.rwin.tag.datamodel.ArtPiece;
import com.rwin.tag.datamodel.Marker;
import com.rwin.tag.datamodel.User;
import com.rwin.tag.ui.layout.MarkerOverlayItem;
import com.rwin.tag.ui.maps.ArtMapView;
import com.rwin.tag.ui.maps.ArtOverlay;
import com.rwin.tag.ui.maps.ArtOverlay.MarkerSelectionListener;
import com.rwin.tag.ui.maps.LocationOverlay;

public class Randi extends Activity implements
        OnItemGestureListener<MarkerOverlayItem> {
    private SimpleLocationTracker mTracker;
    private ArtMapView mMapView;
    private CompassOverlay mCompassOverlay;
    private LocationOverlay mLocationOverlay;
    private ItemizedOverlayWithFocus<MarkerOverlayItem> itemOverlay;
    // private RotationGestureOverlay mRotationGestureOverlay;
    private static final String TAG = "com.rwin.randy.Randi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;

        // setContentView(R.layout.activity_randi);
        // mMapView = (ArtMapView) findViewById(R.id.artMapView);
        mMapView = new ArtMapView(context);
        this.mTracker = new SimpleLocationTracker(this);
        mMapView.getController().setCenter(
                new GeoPoint(mTracker.getLastKnownLocation()));

        this.mCompassOverlay = new CompassOverlay(context,
                new InternalCompassOrientationProvider(context), mMapView);
        this.mCompassOverlay.enableCompass();
        this.mLocationOverlay = new LocationOverlay(context, mMapView, mTracker);
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();

        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        mMapView.getOverlays().add(this.mLocationOverlay);

        itemOverlay = new ItemizedOverlayWithFocus<MarkerOverlayItem>(context,
                new ArrayList<MarkerOverlayItem>(), this);
        mMapView.getOverlays().add(itemOverlay);

        mMapView.getController().setZoom(10);
        mLocationOverlay.enableMyLocation();

        this.setContentView(mMapView);

        final DataModel model = DataController.getInstance().getModel();
        model.addPropertyChangeListener("markers",
                new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent event) {
                        itemOverlay.removeAllItems();
                        for (Marker m : model.getMarkers()) {
                            itemOverlay.addItem(new MarkerOverlayItem(m));
                        }
                    }
                });
    }

    @Override
    public boolean onItemSingleTapUp(int paramInt, MarkerOverlayItem paramT) {
        itemOverlay.setFocusedItem(paramInt);
        return true;
    }

    @Override
    public boolean onItemLongPress(int paramInt, MarkerOverlayItem paramT) {
        // TODO Auto-generated method stub
        return false;
    }
}
