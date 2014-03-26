package com.rwin.tag.api;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.geojson.GeoJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rwin.tag.datamodel.ArtPiece;
import com.rwin.tag.datamodel.Marker;
import com.rwin.tag.datamodel.User;
import com.rwin.tag.datastore.DataStore;
import com.rwin.tag.util.OpenTile;

@Path("/v1/marker/")
public class MarkerResource {

    public static Logger LOG = LoggerFactory.getLogger(MarkerResource.class);

    @OPTIONS
    public Response options() {
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GeoJsonObject getMarkers(@QueryParam("latn") double latNorth,
            @QueryParam("lone") double lonEast,
            @QueryParam("lats") double latSouth,
            @QueryParam("lonw") double lonWest) {
        LOG.info("getMarkers: latNorth: " + latNorth + " lonEast: " + lonEast
                + " latSouth: " + latSouth + " lonWest: " + lonWest);

        if (latNorth < latSouth) {
            LOG.info("latn < lats");
            throw new BadRequestException("latn < lats");
        }
        if (lonWest > lonEast) {
            LOG.info("lonw > lone");
            throw new BadRequestException("lone > lonw");
        }
        return DataStore.getInstance().getMarkersAsGeoJson(latNorth, lonEast, latSouth,
                lonWest);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Marker placeMarker(@QueryParam("lat") double lat,
            @QueryParam("lon") double lon, @QueryParam("id") String id,
            @QueryParam("owner") String name) {
        int zoom = DataStore.getInstance().getZoom();
        int x = OpenTile.getXTile(lon, zoom);
        int y = OpenTile.getYTile(lat, zoom);

        ArtPiece a = DataStore.getInstance().getArtPiece(id);
        if (a == null)
            throw new NotFoundException("Art: " + id + " does not exist");

        User u = DataStore.getInstance().getUser(name);
        if (u == null)
            throw new NotFoundException("User: " + name + " does not exist");

        Marker m = new Marker(x, y, a, u);
        DataStore.getInstance().addMarker(m);
        DataStore.getInstance().addPolygon(m.polygon);
        return m;
    }

}
