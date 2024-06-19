package org.example.springbootintro;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

public class TestUtils {
    public static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
