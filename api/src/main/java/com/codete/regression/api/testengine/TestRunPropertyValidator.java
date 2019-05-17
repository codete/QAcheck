package com.codete.regression.api.testengine;

public class TestRunPropertyValidator {

    public static final int APP_NAME_MAX_LENGTH = 100;
    public static final int APP_NAME_MIN_LENGTH = 5;
    // The length is related to the maximum length of the HTTP URL - crawler will use http url as the test name
    public static final int TEST_NAME_MAX_LENGTH = 2083;
    public static final int TEST_NAME_MIN_LENGTH = 5;
    private static final String NULL_PROPERTY_MESSAGE = "%s cannot be empty or null.";
    private static final String PROPERTY_LENGTH_ERROR_MESSAGE = "%s length should be between %d and %d.";

    public static void validate(String value, String propertyName, int minLength, int maxLength) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(String.format(NULL_PROPERTY_MESSAGE, propertyName));
        }
        if (value.length() < minLength || value.length() > maxLength) {
            throw new IllegalArgumentException(
                    String.format(PROPERTY_LENGTH_ERROR_MESSAGE, propertyName, minLength, maxLength));
        }
    }
}
