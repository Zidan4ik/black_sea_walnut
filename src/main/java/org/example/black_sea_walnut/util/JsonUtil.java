package org.example.black_sea_walnut.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonUtil {
    @SneakyThrows
    public static <K> K parseObject(String object, Class<K> clazz) {
        ObjectMapper jsonParse = new ObjectMapper();
        return jsonParse.readValue(object, clazz);
    }
}
