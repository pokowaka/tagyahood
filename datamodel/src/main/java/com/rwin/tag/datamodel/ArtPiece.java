package com.rwin.tag.datamodel;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rwin.tag.util.Util;

public class ArtPiece {

    public enum ArtType {
        Piece, Tag, ThrowUp,
    }

    public static ArtPiece getTag(Object img) {
        ArtPiece art = new ArtPiece();
        art.type = ArtType.Tag;
        art.id = UUID.randomUUID();
        art.url = "http://192.168.1.178:8080/v1/art/" + art.id;
        art.img = img;
        return art;

    }

    public static ArtPiece parse(String artPiece) {
        return Util.parse(artPiece, ArtPiece.class);
    }

    public UUID id;
    @JsonIgnore
    public Object img;
    public ArtType type;
    public String url;
    public long created;
    public int ups;
    public int downs;

    public int score() {
        return ups - downs;
    }

    // Reddit's voting http://amix.dk/blog/post/19588
    public double confidence() {
        int n = ups + downs;
        if (n == 0)
            return 0;

        float z = 1.0f; // 1.0 = 85%, 1.6 = 95%
        float phat = (float) ups / n;
        return Math.sqrt(phat + z * z / (2 * n) - z
                * ((phat * (1 - phat) + z * z / (4 * n)) / n))
                / (1 + z * z / n);
    }

    @Override
    public String toString() {
        return Util.toJsonString(this);
    }
}
