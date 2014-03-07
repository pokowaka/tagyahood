package com.rwin.tag.datastore;

import java.awt.image.RenderedImage;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserTag extends ArtPiece {

    @JsonIgnore
    RenderedImage img;

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }
}
