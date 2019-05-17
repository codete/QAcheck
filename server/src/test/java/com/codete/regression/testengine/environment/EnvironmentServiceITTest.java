package com.codete.regression.testengine.environment;

import com.codete.regression.api.screenshot.EnvironmentSettings;
import com.codete.regression.config.TestApplicationConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.persistence.Table;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class EnvironmentServiceITTest extends TestApplicationConfig {

    private static final String ENVIRONMENT_TABLE_NAME = Environment.class.getAnnotation(Table.class).name();
    private static final EnvironmentSettings ENVIRONMENT_SETTINGS = createEnvironmentSettings();

    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldCreateNewEnvironmentIfItDoesNotExist() {
        int rowsNumberBeforeSave = countRows();

        environmentService.findByEnvironmentSettingsOrCreateNewOne(ENVIRONMENT_SETTINGS);

        int rowsNumberAfterSave = countRows();
        assertThat(rowsNumberAfterSave - rowsNumberBeforeSave, is(1));
    }

    @Test
    void shouldReuseExistingEnvironmentIfItExists() {
        Environment createdEnvironment = environmentService.findByEnvironmentSettingsOrCreateNewOne(ENVIRONMENT_SETTINGS);
        int rowsNumberBeforeSave = countRows();
        Environment existingEnvironment = environmentService.findByEnvironmentSettingsOrCreateNewOne(ENVIRONMENT_SETTINGS);

        int rowsNumberAfterSave = countRows();
        assertThat(rowsNumberAfterSave, is(rowsNumberBeforeSave));
        assertThat(createdEnvironment.getId(), is(existingEnvironment.getId()));
    }

    private static EnvironmentSettings createEnvironmentSettings() {
        EnvironmentSettings environmentSettings = new EnvironmentSettings();
        environmentSettings.setOs("OS_NAME");
        environmentSettings.setBrowser("BROWSER");
        environmentSettings.setViewPortHeight(10);
        environmentSettings.setViewPortWidth(10);
        return environmentSettings;
    }

    private int countRows() {
        return JdbcTestUtils.countRowsInTable(jdbcTemplate, ENVIRONMENT_TABLE_NAME);
    }

}