package com.rwin.tag.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rwin.tag.datamodel.ArtPiece;
import com.rwin.tag.datamodel.Crew;
import com.rwin.tag.datamodel.Marker;
import com.rwin.tag.datamodel.MarkerPolygon;
import com.rwin.tag.datamodel.User;

/**
 * @author erwinj
 * 
 */
public class DataStore {

    private static final Logger LOG = LoggerFactory.getLogger(DataStore.class);

    static DataStore instance = new DataStore();

    private ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<String, User>();
    private ConcurrentHashMap<String, Crew> crewMap = new ConcurrentHashMap<String, Crew>();
    private ConcurrentHashMap<String, ArtPiece> artMap = new ConcurrentHashMap<String, ArtPiece>();
    private ConcurrentHashMap<Long, Marker> tags = new ConcurrentHashMap<Long, Marker>();
    private ConcurrentHashMap<Long, MarkerPolygon> poly = new ConcurrentHashMap<Long, MarkerPolygon>();

    public static DataStore getInstance() {
        return instance;
    }

    public void addMarker(Marker t) {
        tags.put(t.marker_id, t);
        User u = userMap.get(t.owner);
        u.markers.add(t);
    }

    public void removeMarker(double lat, double lon) {
        Marker toRemove = null;
        for (Entry<Long, Marker> entry : tags.entrySet()) {
            Marker m = entry.getValue();
            if (m.lat == lat && m.lon == lon) {
                toRemove = m;
                break;
            }
        }

        if (toRemove != null) {
            User u = userMap.get(toRemove.owner);
            u.fame += toRemove.calculateFame(u.lastUpdated,
                    System.currentTimeMillis());
            tags.remove(toRemove.marker_id);
        }
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

        for (Entry<Long, Marker> entry : tags.entrySet()) {
            Marker t = entry.getValue();
            if (latSouth <= t.lat || t.lat <= latNorth || lonEast <= t.lon
                    || t.lon <= lonWest)
                found.add(t);
        }
        return found;
    }

    public int getZoom() {
        return 18;
    }

    public Crew getCrew(String name) {
        return crewMap.get(name);
    }

    public void addCrew(Crew c) {
        crewMap.put(c.name, c);
    }

    /**
     * Updates the fame of every user.. Do this ever so often..
     */
    public void updateFame() {
        for (User u : userMap.values()) {
            long update = System.currentTimeMillis();
            long from = u.lastUpdated;
            u.lastUpdated = update;
            for (Marker m : u.markers) {
                u.fame += m.calculateFame(from, update);
            }
        }
    }

    public Marker getTag(long m1) {
        return tags.get(m1);
    }

    AtomicLong idGen = new AtomicLong();

    public long getNextId() {
        return idGen.incrementAndGet();
    }

    public MarkerPolygon getPoly(long polygon_id) {
        return poly.get(polygon_id);
    }

    public void add(MarkerPolygon poly) {
        this.poly.put(poly.id, poly);
    }

    public void removePoly(MarkerPolygon markerPolygon) {
      this.poly.remove(markerPolygon.id);
    }
}
