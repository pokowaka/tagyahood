package com.rwin.tag.api;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rwin.tag.datamodel.Crew;
import com.rwin.tag.datamodel.Marker;
import com.rwin.tag.datamodel.MarkerPolygon;
import com.rwin.tag.datastore.DataStore;

@Path("/v1/polygon/")
public class PolygonResource {

    public static Logger LOG = LoggerFactory.getLogger(PolygonResource.class);

    @OPTIONS
    public Response options() {
        return Response.ok().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public MarkerPolygon combine(@QueryParam("m1") long m1,
            @QueryParam("m2") long m2, @QueryParam("crew") String crew) {

        DataStore store = DataStore.getInstance();

        Crew c = store.getCrew(crew);
        if (c == null)
            throw new NotFoundException("Crew does not exists");

        Marker fst = store.getTag(m1);
        if (fst == null)
            throw new NotFoundException("Marker m1 does not exist");

        Marker snd = store.getTag(m2);
        if (snd == null)
            throw new NotFoundException("Marker m2 does not exist");

        return null;
    }

}
