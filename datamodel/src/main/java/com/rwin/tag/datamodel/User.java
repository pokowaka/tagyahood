package com.rwin.tag.datamodel;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rwin.tag.util.Util;

public class User {

    public String name;

    @JsonIgnore
    public String passwd;

    public ArtPiece tag;

    @JsonIgnore
    public Collection<Marker> markers;

    public User() {
    }

    public User(String name, String passwd, ArtPiece tag) {
        super();
        this.name = name;
        this.passwd = passwd;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return Util.toJsonString(this);
    }
    

    public static User parse(String content) {
        return Util.parse(content, User.class);
    }
}
