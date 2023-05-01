package com.orsolon.recipewebservice.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LombokTestUtil {

    public static <T> void testEqualsAndHashCode(T obj1, T obj2, T obj3) {
        assertTrue(obj1.equals(obj2));
        assertTrue(obj2.equals(obj1));
        assertFalse(obj1.equals(obj3));
        assertFalse(obj3.equals(obj1));
        assertFalse(obj2.equals(obj3));
        assertFalse(obj3.equals(obj2));

        assertEquals(obj1.hashCode(), obj2.hashCode());
        assertNotEquals(obj1.hashCode(), obj3.hashCode());
    }

    public static <T> void testToString(T obj) {
        assertNotNull(obj.toString());
    }
}
