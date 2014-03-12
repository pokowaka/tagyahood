package com.rwin.tag.datamodel;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rwin.tag.util.Util;

public class Crew {

    public String name;
    public String description;
    Collection<ArtPiece> pieces;
    Collection<ArtPiece> throwUps;
    public int color;

    public Crew(String name) {
        super();
        this.name = name;
    }

    public Crew() {
        // TODO Auto-generated constructor stub
    }

    @JsonIgnore
    public Object img;

    @Override
    public String toString() {
        return Util.toJsonString(this);
    }

    public static Crew parse(String content) {
        return Util.parse(content, Crew.class);
    }
}
