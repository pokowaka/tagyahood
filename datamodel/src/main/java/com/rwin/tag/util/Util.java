package com.rwin.tag.util;

import java.io.StringWriter;

import com.fasterxml.jackson.core.type.TypeReference;
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

    public static <T> T parse(final String content, final Class<T> type) {
        T data = null;

        try {
            data = new ObjectMapper().readValue(content, type);
        } catch (Exception e) {
            // Nothing for now
            throw new RuntimeException(e);
        }
        return data;
    }

    public static <T> T parse(final String content, final TypeReference<T> type) {
        T data = null;

        try {
            data = new ObjectMapper().readValue(content, type);
        } catch (Exception e) {
            // Nothing for now
            throw new RuntimeException(e);
        }
        return data;
    }
}
