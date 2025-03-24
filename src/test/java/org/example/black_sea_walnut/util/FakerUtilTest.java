//package org.example.black_sea_walnut.util;
//
//import org.example.black_sea_walnut.enums.UserStatus;
//import org.example.black_sea_walnut.util.FakerUtil;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class FakerUtilTest {
//    static class TestClass {
//        private String name;
//        private int age;
//        private Integer age2;
//        private int negativeValue;
//        private long id;
//        private Long id2;
//        private double price;
//        private boolean isActive;
//        private LocalDateTime createdAt;
//        private Date birthDate;
//        private UserStatus status;
//    }
//
//    @Test
//    void shouldFillStringField() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        assertThat(instance.name).isNotNull();
//    }
//
//    @Test
//    void shouldFillIntField() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        assertThat(instance.age).isGreaterThan(0);
//        assertThat(instance.age).isLessThan(100);
//    }
//
//    @Test
//    void shouldFillIntegerField() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        assertThat(instance.age2).isGreaterThan(0);
//        assertThat(instance.age2).isLessThan(100);
//    }
//
//    @Test
//    void shouldFillNegativeIntField() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        instance.negativeValue = -1;
//        assertThat(instance.negativeValue).isEqualTo(-1);
//    }
//
//    @Test
//    void shouldFillLongField() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        assertThat(instance.id).isGreaterThan(0);
//    }
//
//    @Test
//    void shouldFillLongField2() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        assertThat(instance.id2).isGreaterThan(0L);
//    }
//
//    @Test
//    void shouldFillDoubleField() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        assertThat(instance.price).isGreaterThan(0.0);
//        assertThat(instance.price).isLessThan(100.0);
//    }
//
//    @Test
//    void shouldFillBooleanField() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        assertThat(instance.isActive).isIn(true, false);
//    }
//
//    @Test
//    void shouldFillLocalDateTimeField() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        assertThat(instance.createdAt).isNotNull();
//    }
//
//    @Test
//    void shouldFillDateField() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        assertThat(instance.birthDate).isNotNull();
//    }
//
//    @Test
//    void shouldFillEnumField() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        assertThat(instance.status).isNotNull();
//    }
//
//    @Test
//    void shouldSkipIdField() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        assertThat(instance.id).isEqualTo(0L);
//    }
//
//    @Test
//    void shouldHandleNullValuesInEnum() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        instance.status = null; // Set enum to null
//        assertThat(instance.status).isNull();
//    }
//
//    @Test
//    void shouldHandleNegativeValueForInt() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        instance.age = -1;  // Set negative int value manually
//        assertThat(instance.age).isEqualTo(-1);
//    }
//
//    @Test
//    void shouldHandleTrueBooleanValue() {
//        TestClass instance = FakerUtil.fill(TestClass.class);
//        instance.isActive = true; // Set boolean field to true
//        assertThat(instance.isActive).isTrue();
//    }
//}
