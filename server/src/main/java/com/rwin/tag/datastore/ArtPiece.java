package com.rwin.tag.datastore;

import java.awt.image.RenderedImage;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rwin.tag.util.Util;

public class ArtPiece {

    public enum ArtType {
        Piece, Tag, ThrowUp,
    }

    public static ArtPiece getTag(RenderedImage img) {
        ArtPiece art = new ArtPiece();
        art.type = ArtType.Tag;
        art.id = UUID.randomUUID();
        art.url = "http://192.168.1.178:8080/v1/art/" + art.id;
        art.img = img;
        return art;

    }

    public UUID id;

    public ArtType type;

    public String url;

    @JsonIgnore
    public RenderedImage img;

    @Override
    public String toString() {
        return Util.toJsonString(this);
    }
}
