package com.rwin.tag.datastore;

import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.rwin.tag.tile.OpenTile;

public class DataStore {

    static DataStore instance = new DataStore();

    private ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<String, User>();

    public static DataStore getInstance() {
        return instance;
    }

    public RenderedImage getImage(String name) {
        User user = getUser(name);
        if (user != null) 
            return user.img;
        return null;
    }

    Collection<Marker> tags = Collections
            .synchronizedCollection(new ArrayList<Marker>());

    public void addMarker(Marker t) {
        tags.add(t);
    }

    public synchronized List<Marker> getMarkers(int zoom, int x, int y) {
        OpenTile bounds = OpenTile.tile2BB(x, y, zoom, Tag.MAX_ZOOM);
        List<Marker> found = new ArrayList<Marker>();

        // TODO(ErwinJ): Replace with db query :-)
        for (Marker t : tags) {
            if (bounds.south <= t.x && t.x <= bounds.north
                    && bounds.east <= t.y && t.y <= bounds.west) {
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

}
