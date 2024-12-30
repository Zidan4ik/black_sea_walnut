package org.example.black_sea_walnut.util;

import com.github.javafaker.Faker;
import java.lang.reflect.Field;
import java.util.Random;

public class FakerUtil {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    public static <T> T fill(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                if ("id".equals(field.getName())) {
                    continue;
                }
                if (field.getType().equals(String.class)) {
                    field.set(instance, faker.lorem().word());
                } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                    int value = random.nextInt(100);
                    field.set(instance, value > 0 ? value : 1);
                } else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
                    field.set(instance, faker.number().randomNumber());
                } else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
                    field.set(instance, random.nextDouble() * 100);
                } else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                    field.set(instance, random.nextBoolean());
                } else if (field.getType().equals(java.time.LocalDateTime.class)) {
                    field.set(instance, faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                } else if (field.getType().equals(java.util.Date.class)) {
                    field.set(instance, faker.date().birthday());
                } else if (field.getType().isEnum()) {
                    Object[] enumValues = field.getType().getEnumConstants();
                    Object value = enumValues[random.nextInt(enumValues.length)];
                    field.set(instance, value);
                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Cannot fill class: " + clazz.getName(), e);
        }
    }

}
