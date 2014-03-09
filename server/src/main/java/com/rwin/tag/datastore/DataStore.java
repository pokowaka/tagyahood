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

    public void addUser(User user) {
        this.userMap.put(user.name, user);
    }

    public User getUser(String name) {
        return this.userMap.get(name);
    }

    public List<Marker> getMarkers(double latNorth, double lonEast,
            double latSouth, double lonWest) {
        List<Marker> found = new ArrayList<Marker>();

        for (Marker t : tags) {
            if (latSouth <= t.lat || t.lat <= latNorth || lonEast <= t.lon
                    || t.lon <= lonWest)
                found.add(t);
        }
        return found;
    }

    public int getZoom() {
        return 18;
    }

}
