package com.rwin.tag.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rwin.tag.datastore.DataStore;
import com.rwin.tag.datastore.Marker;
import com.rwin.tag.datastore.User;

@Path("/v1/marker/")
public class MarkerResource {

    public static Logger log = LoggerFactory.getLogger(MarkerResource.class);

    @OPTIONS
    public Response options() {
        return Response.ok().build();
    }

    @GET
    @Path("{zoom}/{x}/{y}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Marker> getMarkers(@PathParam("zoom") int zoom,
            @PathParam("x") int x, @PathParam("y") int y) {
        return DataStore.getInstance().getMarkers(zoom, x, y);
    }

    @PUT
    @Path("{zoom}/{x}/{y}")
    @Produces(MediaType.APPLICATION_JSON)
    public Marker placeMarker(@PathParam("zoom") int zoom,
            @PathParam("x") int x, @PathParam("y") int y,
            @QueryParam("id") String name) {
        if (zoom != Marker.MAX_ZOOM)
            throw new IllegalArgumentException("Zoom level has to be "
                    + Marker.MAX_ZOOM);

        User u = DataStore.getInstance().getUser(name);
        if (u == null)
            throw new NotFoundException("User: " + name + " does not exist");

        Marker m = new Marker(x, y, u.tag, u.name);
        DataStore.getInstance().addMarker(m);
        return m;
    }

}
