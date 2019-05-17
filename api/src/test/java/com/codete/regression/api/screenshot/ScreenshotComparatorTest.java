package com.codete.regression.api.screenshot;

import com.codete.regression.api.screenshot.drivers.ScreenshotTakerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ScreenshotComparatorTest {

    @Test
    void shouldValidateAppNameLengthAndThrowsExceptionWhenItIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> new ScreenshotComparator("http://localhost:8080", "apiKey", "app", ScreenshotTakerType.BROWSER));
    }

    @Test
    void shouldValidateTestNameLengthAndThrowsExceptionWhenItIsInvalid() {
        ScreenshotComparator screenshotComparator = new ScreenshotComparator("http://localhost:8080",
                "apiKey", "appNameProperLength", ScreenshotTakerType.BROWSER);
        assertThrows(IllegalArgumentException.class,
                () -> screenshotComparator.compareScreenshotWithBaseline(null, "test"));
    }

}