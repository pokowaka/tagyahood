package com.rwin.tag.datamodel;

import java.util.Collection;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rwin.tag.util.Util;

public class User {

    public static User parse(String content) {
        return Util.parse(content, User.class);
    }

    public int color;
    public int fame;
    @JsonIgnore
    public Collection<Marker> markers = new LinkedList<Marker>();
    public String name;
    @JsonIgnore
    public String passwd;
    public ArtPiece tag;
    public Crew crew;

    // Last time when the users fame was updated.
    public long lastUpdated;

    public User() {
    }

    public User(String name, String passwd, Crew c, ArtPiece tag) {
        super();
        this.name = name;
        this.passwd = passwd;
        this.tag = tag;
        this.crew = c;
    }

    @Override
    public String toString() {
        return Util.toJsonString(this);
    }
}
