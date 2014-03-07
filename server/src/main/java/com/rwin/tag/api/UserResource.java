package com.rwin.tag.api;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rwin.tag.datastore.DataStore;
import com.rwin.tag.datastore.Marker;
import com.rwin.tag.datastore.User;

@Path("/v1/user/")
public class UserResource {

    private static final Logger LOG = LoggerFactory
            .getLogger(UserResource.class);

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("name") String name) {
        User u = getUserFromStore(name);
        return u;
    }

    @GET
    @Path("{name}/marker")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Marker> getMarkers(@PathParam("name") String name) {
        User u = getUserFromStore(name);
        return u.markers;
    }

    private User getUserFromStore(String name) {
        User u = DataStore.getInstance().getUser(name);
        if (u == null)
            throw new NotFoundException("User: " + name + " does not exist");
        return u;
    }

    @GET
    @Path("{name}/img")
    @Produces("image/png")
    public RenderedImage getUserTag(@PathParam("name") String name) {
        User u = getUserFromStore(name);
        return u.img;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public User createUser(@FormDataParam("picture") InputStream entityStream,
            @FormDataParam("name") String name,
            @FormDataParam("passwd") String passwd) throws IOException {
        RenderedImage img = ImageIO.read(entityStream);
        User user = new User(name, passwd, img);
        DataStore.getInstance().addUser(user);

        LOG.info("createUser: entityStream: " + entityStream + ", name: "
                + name + ", passwd: " + passwd + ", user:" + user);
        return user;
    }
}