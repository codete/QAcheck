package com.codete.regression.testengine.environment;

import com.codete.regression.api.screenshot.EnvironmentSettings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@Entity
@SequenceGenerator(name = "environment_id_seq", sequenceName = "environment_id_seq", allocationSize = 1)
@Table(name = "environment")
public class Environment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "environment_id_seq")
    private long id;

    @NotNull
    private String os;

    @NotNull
    private String browser;

    private int viewPortWidth;

    private int viewPortHeight;

    public Environment(EnvironmentSettings environmentSettings) {
        this.os = environmentSettings.getOs();
        this.browser = environmentSettings.getBrowser();
        this.viewPortHeight = environmentSettings.getViewPortHeight();
        this.viewPortWidth = environmentSettings.getViewPortWidth();
    }

    public EnvironmentSettings createEnvironmentSettings() {
        EnvironmentSettings environmentSettings = new EnvironmentSettings();
        environmentSettings.setOs(os);
        environmentSettings.setBrowser(browser);
        environmentSettings.setViewPortWidth(viewPortWidth);
        environmentSettings.setViewPortHeight(viewPortHeight);
        return environmentSettings;
    }
}
