package com.rwin.tag.datastore;

import java.awt.image.RenderedImage;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

    public String name;
    
    @JsonIgnore
    public String passwd;

    @JsonIgnore
    public RenderedImage img;

    @JsonIgnore
    public Collection<Marker> markers;

    public User() {
    }

    public User(String name, String passwd, RenderedImage img) {
        super();
        this.name = name;
        this.passwd = passwd;
        this.img = img;
    }

}
