package com.rwin.tag.datamodel;

import java.util.Collection;

import com.rwin.tag.util.Util;

public class Crew {

    public String name;

    public String description;

    Collection<ArtPiece> pieces;

    Collection<ArtPiece> throwUps;

    public int color;

    Object image;

    @Override
    public String toString() {
        return Util.toJsonString(this);
    }

    public static Crew parse(String content) {
        return Util.parse(content, Crew.class);
    }
}
