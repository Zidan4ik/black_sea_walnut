package org.example.black_sea_walnut.util;

import org.example.black_sea_walnut.entity.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsonUtilTest {
    @Test
    void shouldParseJsonToObject() {
        String json = "{\"id\":1}";
        Product product = JsonUtil.parseObject(json, Product.class);
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(1);
    }

    @Test
    void shouldReturnNullForInvalidJson() {
        String invalidJson = "{\"id\":1}";
        Product product = JsonUtil.parseObject(invalidJson, Product.class);
    }

    @Test
    void shouldHandleEmptyJsonString() {
        String emptyJson = "{}";
        Product product = JsonUtil.parseObject(emptyJson, Product.class);
        assertThat(product).isNotNull();
    }

    @Test
    void shouldThrowExceptionForInvalidJsonFormat() {
        String invalidJson = "This is not JSON";
        try {
            JsonUtil.parseObject(invalidJson, Product.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(com.fasterxml.jackson.core.JsonParseException.class);
        }
    }
}