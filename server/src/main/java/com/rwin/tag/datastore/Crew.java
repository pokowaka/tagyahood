package com.rwin.tag.datastore;

import java.awt.image.RenderedImage;
import java.util.Collection;

import com.rwin.tag.util.Util;

public class Crew {

    public String name;

    Collection<ArtPiece> pieces;

    Collection<ArtPiece> throwUps;

    RenderedImage image;

    @Override
    public String toString() {
        return Util.toJsonString(this);
    }
}
