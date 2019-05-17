package com.codete.regression.crawler.browserstack;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString(exclude = "realMobile")
public class Browser {

    private String os;

    @JsonAlias({"os_version", "osVersion"})
    private String osVersion;

    private String browser;

    @JsonAlias({"browser_version", "browserVersion"})
    private String browserVersion;

    @JsonAlias({"real_mobile", "realMobile"})
    private boolean realMobile;

    @JsonProperty("osVersion")
    public String getOsVersion() {
        return osVersion;
    }

    @JsonProperty("browserVersion")
    public String getBrowserVersion() {
        return browserVersion;
    }

    @JsonProperty("realMobile")
    public boolean isRealMobile() {
        return realMobile;
    }
}
