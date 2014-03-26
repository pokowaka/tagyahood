package com.rwin.tag.api;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MarkerPolygon> getMarkerPolygons(
            @QueryParam("latn") double latNorth,
            @QueryParam("lone") double lonEast,
            @QueryParam("lats") double latSouth,
            @QueryParam("lonw") double lonWest) {
        LOG.info("getMarkerPolygons: latNorth: " + latNorth + " lonEast: "
                + lonEast + " latSouth: " + latSouth + " lonWest: " + lonWest);

        if (latNorth < latSouth) {
            LOG.info("latn < lats");
            throw new BadRequestException("latn < lats");
        }
        if (lonWest > lonEast) {
            LOG.info("lonw > lone");
            throw new BadRequestException("lone > lonw");
        }
        return DataStore.getInstance().getPolygons(latNorth, lonEast, latSouth,
                lonWest);
    }

    @POST
    @Path("/combine")
    @Produces(MediaType.APPLICATION_JSON)
    public MarkerPolygon combine(@QueryParam("m1") long m1,
            @QueryParam("m2") long m2) {

        DataStore store = DataStore.getInstance();

        Marker fst = store.getTag(m1);
        if (fst == null)
            throw new NotFoundException("Marker m1 does not exist");

        Marker snd = store.getTag(m2);
        if (snd == null)
            throw new NotFoundException("Marker m2 does not exist");

        return MarkerPolygon.combine(fst, snd);

    }

    @POST
    @Path("/split")
    @Produces(MediaType.APPLICATION_JSON)
    public MarkerPolygon[] split(@QueryParam("m1") long m1,
            @QueryParam("m2") long m2) {

        DataStore store = DataStore.getInstance();

        Marker fst = store.getTag(m1);
        if (fst == null)
            throw new NotFoundException("Marker m1 does not exist");

        Marker snd = store.getTag(m2);
        if (snd == null)
            throw new NotFoundException("Marker m2 does not exist");

        if (fst.polygon != snd.polygon)
            throw new BadRequestException("Markers are not in the same polygon");

        int idxFst = fst.polygon.indexOf(fst);
        int idxSnd = snd.polygon.indexOf(snd);
        if (Math.abs(idxFst - idxSnd) > 1 || idxFst == -1 || idxSnd == -1)
            throw new BadRequestException("Markers are not next to each other.");
        MarkerPolygon[] poly = new MarkerPolygon[2];
        poly[0] = fst.polygon;
        poly[1] = (idxFst < idxSnd) ? fst.polygon.splitAfter(fst) : fst.polygon
                .splitBefore(fst);
        return poly;
    }
}
