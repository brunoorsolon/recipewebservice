package com.orsolon.recipewebservice.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LombokTestUtil {

    public static <T> void testEqualsAndHashCode(T obj1, T obj2, T obj3) {
        assertEquals(obj1, obj2);
        assertEquals(obj2, obj1);
        assertNotEquals(obj1, obj3);
        assertNotEquals(obj3, obj1);
        assertNotEquals(obj2, obj3);
        assertNotEquals(obj3, obj2);

        assertEquals(obj1.hashCode(), obj2.hashCode());
        assertNotEquals(obj1.hashCode(), obj3.hashCode());
    }

    public static <T> void testToString(T obj) {
        assertNotNull(obj.toString());
    }
}
