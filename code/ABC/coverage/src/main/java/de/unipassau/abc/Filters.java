package de.unipassau.abc;

import java.util.Arrays;
import java.util.List;

public class Filters {
    public static List<String> blackListedClassPrefixes = Arrays.asList(
          "android",
          "com.google"
    );

    public static boolean isAllowedClass(String className) {
        return blackListedClassPrefixes.stream().noneMatch(className::startsWith);
    }
}
