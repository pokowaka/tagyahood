package com.mapbox.mapboxsdk.tileprovider.tilesource;

import android.graphics.drawable.Drawable;

import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.tileprovider.MapTile;
import com.mapbox.mapboxsdk.tileprovider.constants.TileLayerConstants;
import com.mapbox.mapboxsdk.tileprovider.modules.MapTileDownloader;
import com.mapbox.mapboxsdk.views.util.constants.MapViewConstants;

public class TileLayer implements ITileLayer, TileLayerConstants, MapViewConstants {

    protected String mUrl;
    protected String mName;
    protected String mDescription;
    protected String mAttribution;
    protected String mLegend;

    protected float mMinimumZoomLevel = TileLayerConstants.MINIMUM_ZOOMLEVEL;
    protected float mMaximumZoomLevel = TileLayerConstants.MAXIMUM_ZOOMLEVEL;
    protected BoundingBox mBoundingBox = null;
    protected LatLng mCenter = new LatLng(0, 0);
    private final int mTileSizePixels = DEFAULT_TILE_SIZE;

    public TileLayer(final String aUrl) {
        mUrl = aUrl;
    }

    /**
     * Sets the layer's tile URL template string.
     * @param aUrl
     * @return
     */
    public TileLayer setURL(final String aUrl) {
        mUrl = aUrl;
        return this;
    }

    /**
     * Sets the layer's attribution string.
     * @param aAttribution
     * @return
     */
    public TileLayer setAttribution(final String aAttribution) {
        this.mAttribution = aAttribution;
        return this;
    }

    /**
     * Sets the layer's description string.
     * @param aDescription
     * @return
     */
    public TileLayer setDescription(final String aDescription) {
        this.mDescription = aDescription;
        return this;
    }

    /**
     * Sets the layer's name.
     * @param aName
     * @return
     */
    public TileLayer setName(final String aName) {
        this.mName = aName;
        return this;
    }

    /**
     * Sets the layer's minimum zoom level.
     * @param aMinimumZoomLevel
     * @return
     */
    public TileLayer setMinimumZoomLevel(final float aMinimumZoomLevel) {
        this.mMinimumZoomLevel = aMinimumZoomLevel;
        return this;
    }

    /**
     * Sets the layer's minimum zoom level.
     * @param aMaximumZoomLevel
     * @return
     */
    public TileLayer setMaximumZoomLevel(final float aMaximumZoomLevel) {
        this.mMaximumZoomLevel = aMaximumZoomLevel;
        return this;
    }

    public Drawable getDrawableFromTile(final MapTileDownloader downloader, final MapTile aTile, boolean hdpi) {
        return null;
    }

    @Override
    public void detach() {

    }

    @Override
    public float getMinimumZoomLevel() {
        return mMinimumZoomLevel;
    }

    @Override
    public float getMaximumZoomLevel() {
        return mMaximumZoomLevel;
    }

    @Override
    public int getTileSizePixels() {
        return mTileSizePixels;
    }

    final private String TAG = "OnlineTileSource";

    @Override
    public String getCacheKey() {
        return "";
    }

    @Override
    public BoundingBox getBoundingBox() {
        return mBoundingBox;
    }

    @Override
    public LatLng getCenterCoordinate() {
        return mCenter;
    }

    @Override
    public float getCenterZoom() {
        if (mCenter != null) {
            return (float) mCenter.getAltitude();
        }
        return Math.round(mMaximumZoomLevel + mMinimumZoomLevel) / 2;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public String getAttribution() {
        return mAttribution;
    }

    @Override
    public String getLegend() {
        return mLegend;
    }
}
