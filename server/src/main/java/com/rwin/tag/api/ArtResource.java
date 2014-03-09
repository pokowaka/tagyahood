package com.rwin.tag.api;

import java.awt.image.RenderedImage;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rwin.tag.datamodel.ArtPiece;
import com.rwin.tag.datastore.DataStore;

@Path("/v1/art/")
public class ArtResource {

    private static final Logger LOG = LoggerFactory
            .getLogger(ArtResource.class);

    @GET
    @Path("{id}")
    @Produces("image/png")
    public RenderedImage getArtPiece(@PathParam("id") String id) {
        ArtPiece a = DataStore.getInstance().getArtPiece(id);
        if (a == null)
            throw new NotFoundException("Artpiece does not exist");
        LOG.info("getArtPiece: id: " + id);
        return (RenderedImage) a.img;
    }

}
