package com.codete.regression.testengine.teststep;

import com.codete.regression.api.screenshot.Screenshot;
import com.codete.regression.screenshot.ScreenshotComparator;
import com.codete.regression.screenshot.dynamicareas.DynamicAreaDetector;
import com.codete.regression.testengine.comparisonsettings.ComparisonSettings;
import com.codete.regression.testengine.comparisonsettings.IgnoreArea;
import com.codete.regression.testengine.userdecision.UserDecisionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestStepRunnerTest {

    private final UserDecisionService userDecisionService = mock(UserDecisionService.class);
    private final ScreenshotComparator screenshotComparator = mock(ScreenshotComparator.class);
    private final DynamicAreaDetector dynamicAreaDetector = mock(DynamicAreaDetector.class);

    @InjectMocks
    private TestStepRunner testStepRunner;

    @Test
    void shouldCreateNewComparisonSettingsWhenNewDynamicAreasWereGenerated() {
        when(dynamicAreaDetector.createIgnoreAreasForDynamicElements(any(), any()))
                .thenReturn(Collections.singletonList(mock(IgnoreArea.class)));
        ComparisonSettings comparisonSettings = mock(ComparisonSettings.class);

        TestStep testStep = testStepRunner.runTestStepWithDynamicAreasDetection(comparisonSettings,
                "", Collections.singletonList(mock(Screenshot.class)));

        assertThat(testStep.getComparisonSettings(), not(comparisonSettings));
    }

    @Test
    void shouldUseExistingComparisonSettingsWhenNewDynamicAreasWereNotGenerated() {
        when(dynamicAreaDetector.createIgnoreAreasForDynamicElements(any(), any()))
                .thenReturn(Collections.emptyList());
        ComparisonSettings comparisonSettings = mock(ComparisonSettings.class);

        TestStep testStep = testStepRunner.runTestStepWithDynamicAreasDetection(comparisonSettings,
                "", Collections.singletonList(mock(Screenshot.class)));

        assertThat(testStep.getComparisonSettings(), is(comparisonSettings));
    }

}