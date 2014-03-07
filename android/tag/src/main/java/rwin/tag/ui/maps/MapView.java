package com.rwin.tag.ui.maps;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
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

import android.content.Context;

public class MapView extends org.osmdroid.views.MapView {

    private static MapTileProviderArray getTileProvider(Context context) {
        final IRegisterReceiver registerReceiver = new SimpleRegisterReceiver(
                context);

        // Create a custom tile source
        final ITileSource tileSource = new XYTileSource("Mapnik",
                ResourceProxy.string.mapnik, 1, 18, 256, ".jpg", new String[] {
                // "http://tile.openstreetmap.org/"
                // "http://192.168.1.178:4110/toner/"
                "http://tile.stamen.com/toner/" });

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

        // Create a custom tile provider array with the custom tile source and
        // the custom tile providers
        final MapTileProviderArray tileProviderArray = new MapTileProviderArray(
                tileSource, registerReceiver, new MapTileModuleProviderBase[] {
                        fileSystemProvider, fileArchiveProvider,
                        downloaderProvider });

        return tileProviderArray;
    }

    public MapView(Context context) {
        super(context, 256, new DefaultResourceProxyImpl(context),
                getTileProvider(context));
    }
}
