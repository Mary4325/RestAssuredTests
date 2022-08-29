package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtil() {
    }

    public static JsonNode parseJson(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        try {
            return mapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean compareJson(String s1, String s2) {
        if (s1 == null) {
            return false;
        }
        return JsonUtil.parseJson(s1).equals(JsonUtil.parseJson(s2));
    }


    public static JsonNode getJsonFieldValue(String json, String nodePath) {
        if (json == null || nodePath == null) {
            return null;
        }
        try {
            return mapper.readTree(json).at(nodePath);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
