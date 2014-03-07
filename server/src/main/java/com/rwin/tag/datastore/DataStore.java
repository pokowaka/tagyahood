package com.rwin.tag.datastore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rwin.tag.datamodel.ArtPiece;
import com.rwin.tag.datamodel.Marker;
import com.rwin.tag.datamodel.User;
import com.rwin.tag.tile.OpenTile;

public class DataStore {

    private static final Logger LOG = LoggerFactory.getLogger(DataStore.class);

    static DataStore instance = new DataStore();

    private ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<String, User>();

    private ConcurrentHashMap<String, ArtPiece> artMap = new ConcurrentHashMap<String, ArtPiece>();

    public static DataStore getInstance() {
        return instance;
    }

    Collection<Marker> tags = Collections
            .synchronizedCollection(new ArrayList<Marker>());

    public void addMarker(Marker t) {
        tags.add(t);
    }

    public void addArtPiece(ArtPiece art) {
        artMap.put(art.id.toString(), art);
    }

    public ArtPiece getArtPiece(String id) {
        return artMap.get(id);
    }

    public synchronized Marker getMarker(int zoom, int x, int y) {
        if (zoom != Marker.MAX_ZOOM)
            return null;
        for (Marker t : tags) {
            if (t.x == x && t.y == y)
                return t;
        }
        return null;
    }

    public synchronized List<Marker> getMarkers(int zoom, int x, int y) {
        OpenTile bounds = OpenTile.tile2BB(x, y, zoom, Marker.MAX_ZOOM);
        List<Marker> found = new ArrayList<Marker>();

        if (zoom == Marker.MAX_ZOOM) {
            Marker m = getMarker(zoom, x, y);
            if (m != null)
                found.add(m);
            return found;
        }

        // TODO(ErwinJ): Replace with db query :-)
        for (Marker t : tags) {
            if (bounds.south <= t.y && t.y <= bounds.north
                    && bounds.west <= t.x && t.x <= bounds.east) {
                found.add(t);
            }
        }
        return found;
    }

    public void addUser(User user) {
        this.userMap.put(user.name, user);
    }

    public User getUser(String name) {
        return this.userMap.get(name);
    }

    public List<Marker> getMarkers(int zoom, int x, int y, int width, int height) {
        int w = OpenTile.getXTile(OpenTile.tile2boundingBox(x, y, zoom).west,
                Marker.MAX_ZOOM);
        int e = OpenTile.getXTile(
                OpenTile.tile2boundingBox(x + width, y, zoom).east,
                Marker.MAX_ZOOM);
        int n = OpenTile.getYTile(OpenTile.tile2boundingBox(x, y, zoom).north,
                Marker.MAX_ZOOM);
        int s = OpenTile.getYTile(
                OpenTile.tile2boundingBox(x, y + height, zoom).south,
                Marker.MAX_ZOOM);

        LOG.info("Looking for " + w + " ," + e + "," + n + ", " + s);
        List<Marker> found = new ArrayList<Marker>();

        // 41918 ,41924,101317, 101319
        for (Marker t : tags) {
            // "x":41918,"y":101318
            if (n <= t.y && t.y <= s && w <= t.x && t.x <= e) {
                found.add(t);
            }
        }
        return found;
    }

}
