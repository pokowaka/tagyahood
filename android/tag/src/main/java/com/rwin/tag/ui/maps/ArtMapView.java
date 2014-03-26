package com.rwin.tag.ui.maps;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileFilesystemProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.modules.NetworkAvailabliltyCheck;
import org.osmdroid.tileprovider.modules.TileWriter;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.BoundingBoxE6;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.rwin.tag.data.DataController;
import com.rwin.tag.util.OpenTile;

public class ArtMapView extends org.osmdroid.views.MapView {

    private static MapTileProviderArray getTileProvider(Context context) {
        final IRegisterReceiver registerReceiver = new SimpleRegisterReceiver(
                context);

        // Create a custom tile source
        final ITileSource tileSource = new XYTileSource("Mapnik",
                ResourceProxy.string.mapnik, 1, 18, 256, ".png", new String[] {
                // "http://tile.openstreetmap.org/"
                // "http://192.168.1.178:4110/toner/"
                "https://a.tiles.mapbox.com/v3/examples.map-20v6611k/"
                // "http://tile.stamen.com/toner/" 
                });

        // Create a file cache modular provider

        final TileWriter tileWriter = new TileWriter();
        final MapTileFilesystemProvider fileSystemProvider = new MapTileFilesystemProvider(
                registerReceiver, tileSource);

        MapTileFileArchiveProvider fileArchiveProvider = new MapTileFileArchiveProvider(
                registerReceiver, tileSource);

        // Create a download modular tile provider
        final NetworkAvailabliltyCheck networkAvailabliltyCheck = new NetworkAvailabliltyCheck(
                context);
        final MapTileDownloader downloaderProvider = new MapTileDownloader(
                tileSource, tileWriter, networkAvailabliltyCheck, 8, 40);

        // Create a custom tile provider array with the custom tile source
        // and
        // the custom tile providers
        final MapTileProviderArray tileProviderArray = new MapTileProviderArray(
                tileSource, registerReceiver, new MapTileModuleProviderBase[] {
                        fileSystemProvider, fileArchiveProvider,
                        downloaderProvider });

        return tileProviderArray;
    }

    public Rect getTileBound(Rect reuse, int zoom) {
        Rect res = reuse == null ? new Rect() : reuse;
        final BoundingBoxE6 rect = ArtMapView.this.getBoundingBox();
        res.left = OpenTile.getXTile((double) rect.getLonWestE6() / 1000000d,
                zoom);
        res.top = OpenTile.getYTile((double) rect.getLatNorthE6() / 1000000d,
                zoom);
        res.right = OpenTile.getXTile((double) rect.getLonEastE6() / 1000000d,
                zoom);
        res.bottom = OpenTile.getYTile(
                (double) rect.getLatSouthE6() / 1000000d, zoom);
        return res;
    }

    /**
     * Gets thes the rect of visible tiles..
     * 
     * @return
     */
    public Rect getTileBound(int zoom) {
        return getTileBound(null, zoom);
    }

    public ArtMapView(Context context, AttributeSet attrs) {
        super(context, 256, new DefaultResourceProxyImpl(context),
                getTileProvider(context), null, attrs);

        setMapListener(new MapListener() {

            @Override
            public boolean onScroll(ScrollEvent arg0) {
                return updateLocation();
            }

            @Override
            public boolean onZoom(ZoomEvent arg0) {
                return updateLocation();
            }

            private boolean updateLocation() {
                int zoom = getZoomLevel();
                DataController.getInstance().updateLocation(getBoundingBox());
                return true;
            }
        });
    }

    public ArtMapView(Context context) {
        super(context, 256, new DefaultResourceProxyImpl(context),
                getTileProvider(context));

        setMapListener(new MapListener() {

            @Override
            public boolean onScroll(ScrollEvent arg0) {
                return updateLocation();
            }

            @Override
            public boolean onZoom(ZoomEvent arg0) {
                return updateLocation();
            }

            private boolean updateLocation() {
                int zoom = getZoomLevel();
                DataController.getInstance().updateLocation(getBoundingBox());
                return true;
            }
        });
    }
}
