package com.rwin.tag.util;

import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class Util {

    public static String toJsonString(Object o) {

        StringWriter w = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(w, o);
        } catch (Exception e) {
            return "{ error : \"can't deserialize\"}";
        }
        return w.toString();
    }
}
