package com.rwin.tag.api;

import java.awt.image.RenderedImage;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.rwin.tag.datamodel.ArtPiece;
import com.rwin.tag.datastore.DataStore;

@Path("/v1/art/")
public class ArtResource {

    @GET
    @Path("{id}")
    @Produces("image/png")
    public RenderedImage getUserTag(@PathParam("id") String id) {
        ArtPiece a = DataStore.getInstance().getArtPiece(id);
        if (a == null)
            throw new NotFoundException("Artpiece does not exist");
        return (RenderedImage) a.img;
    }

}
