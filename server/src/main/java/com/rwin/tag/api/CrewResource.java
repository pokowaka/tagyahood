package com.rwin.tag.api;

import java.awt.image.RenderedImage;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rwin.tag.datamodel.Crew;
import com.rwin.tag.datastore.DataStore;

@Path("/v1/crew/")
public class CrewResource {

    private static final Logger LOG = LoggerFactory
            .getLogger(UserResource.class);

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Crew getUser(@PathParam("name") String name) {
        Crew c = getCrewFromStore(name);
        return c;
    }

    private Crew getCrewFromStore(String name) {
        Crew u = DataStore.getInstance().getCrew(name);
        if (u == null)
            throw new NotFoundException("Crew: " + name + " does not exist");
        return u;
    }

    @GET
    @Path("{name}/img")
    @Produces(MediaType.APPLICATION_JSON)
    public RenderedImage getCrewImage(@PathParam("name") String name) {
        Crew u = getCrewFromStore(name);
        return (RenderedImage) u.img;
    }
}
