package com.mapbox.mapboxsdk.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.tileprovider.MapTileCache;
import com.mapbox.mapboxsdk.tileprovider.MapTileRequestState;
import com.mapbox.mapboxsdk.tileprovider.tilesource.ITileLayer;
import com.mapbox.mapboxsdk.tileprovider.tilesource.TileLayer;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.util.TileLoadedListener;
import com.mapbox.mapboxsdk.views.util.TilesLoadedListener;

import java.util.concurrent.atomic.AtomicReference;

import uk.co.senab.bitmapcache.CacheableBitmapDrawable;

/**
 * The {@link MapTileDownloader} loads tiles from an HTTP server.
 */
public class MapTileDownloader extends MapTileModuleLayerBase {
    private static final String TAG = "Tile downloader";

    private final AtomicReference<TileLayer> mTileSource = new AtomicReference<TileLayer>();
    private final AtomicReference<MapTileCache> mTileCache = new AtomicReference<MapTileCache>();

    private final NetworkAvailabilityCheck mNetworkAvailabilityCheck;
    private MapView mapView;
    boolean hdpi;


    public MapTileDownloader(final ITileLayer pTileSource,
                             final MapTileCache pTileCache,
                             final NetworkAvailabilityCheck pNetworkAvailabilityCheck,
                             final MapView mapView) {
        super(NUMBER_OF_TILE_DOWNLOAD_THREADS, TILE_DOWNLOAD_MAXIMUM_QUEUE_SIZE);
        this.mapView = mapView;
        this.mTileCache.set(pTileCache);

        hdpi = mapView.getContext().getResources().getDisplayMetrics().densityDpi > DisplayMetrics.DENSITY_HIGH;

        mNetworkAvailabilityCheck = pNetworkAvailabilityCheck;
        setTileSource(pTileSource);
    }

    public ITileLayer getTileSource() {
        return mTileSource.get();
    }

    public MapTileCache getCache() {
        return mTileCache.get();
    }

    public boolean isNetworkAvailable() {
        return (mNetworkAvailabilityCheck == null || mNetworkAvailabilityCheck.getNetworkAvailable());
    }

    public TilesLoadedListener getTilesLoadedListener() {
        return mapView.getTilesLoadedListener();
    }

    public TileLoadedListener getTileLoadedListener() {
        return mapView.getTileLoadedListener();
    }

    @Override
    public boolean getUsesDataConnection() {
        return true;
    }

    @Override
    protected String getName() {
        return "Online Tile Download Provider";
    }

    @Override
    protected String getThreadGroupName() {
        return "downloader";
    }

    @Override
    protected Runnable getTileLoader() {
        return new TileLoader();
    }

    @Override
    public float getMinimumZoomLevel() {
        TileLayer tileLayer = mTileSource.get();
        return (tileLayer != null ? tileLayer.getMinimumZoomLevel() : MINIMUM_ZOOMLEVEL);
    }

    @Override
    public float getMaximumZoomLevel() {
        TileLayer tileLayer = mTileSource.get();
        return (tileLayer != null ? tileLayer.getMaximumZoomLevel() : MAXIMUM_ZOOMLEVEL);
    }

    @Override
    public BoundingBox getBoundingBox() {
        TileLayer tileLayer = mTileSource.get();
        return (tileLayer != null ? tileLayer.getBoundingBox() : null);
    }

    @Override
    public LatLng getCenterCoordinate() {
        TileLayer tileLayer = mTileSource.get();
        return (tileLayer != null ? tileLayer.getCenterCoordinate() : null);
    }

    @Override
    public float getCenterZoom() {
        TileLayer tileLayer = mTileSource.get();
        return (tileLayer != null ? tileLayer.getCenterZoom() : (getMaximumZoomLevel() + getMinimumZoomLevel()) / 2);
    }

    @Override
    public void setTileSource(final ITileLayer tileSource) {
        if (mTileSource.get() != null) {
            mTileSource.get().detach();
        }
        // We are only interested in TileLayer tile sources
        if (tileSource instanceof TileLayer) {
            mTileSource.set((TileLayer) tileSource);
        } else {
            // Otherwise shut down the tile downloader
            mTileSource.set(null);
        }
    }

    protected class TileLoader extends MapTileModuleLayerBase.TileLoader {

        @Override
        public Drawable loadTile(final MapTileRequestState aState) throws CantContinueException {

            TileLayer tileLayer = mTileSource.get();

            if (tileLayer == null) {
                return null;
            } else {
                return tileLayer.getDrawableFromTile(MapTileDownloader.this, aState.getMapTile(), hdpi);
            }
        }
    }

    private CacheableBitmapDrawable onTileLoaded(CacheableBitmapDrawable pDrawable) {
        return mapView.getTileLoadedListener().onTileLoaded(pDrawable);
    }
}
