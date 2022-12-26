package com.finalproject.hwangjunha_team3.utils;

public class ClassUtils {

    public static <T> T getSafeCastInstance(Object o, Class<T> clazz) {
        return clazz != null && clazz.isInstance(o) ? clazz.cast(o) : null;
    }

    // for test
    public static <T> T getTest(Object o, Class<T> clazz) {
        return clazz.cast(o);
    }
}
