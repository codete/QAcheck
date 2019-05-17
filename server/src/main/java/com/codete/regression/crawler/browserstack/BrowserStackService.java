package com.codete.regression.crawler.browserstack;

import com.codete.regression.crawler.CrawlerRequest;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class BrowserStackService {

    private static final String URL = "https://%s:%s@hub-cloud.browserstack.com/wd/hub";
    private final URL browserStackHub;
    private final BrowserListRetriever browserListRetriever;

    public BrowserStackService(BrowserListRetriever browserListRetriever,
                               BrowserStackAuthentication browserStackAuthentication) throws MalformedURLException {
        this.browserListRetriever = browserListRetriever;
        this.browserStackHub = initializeBrowserStackHubUrl(browserStackAuthentication);
    }

    public WebDriver createRemoteWebDriver(CrawlerRequest crawlerRequest) {
        if (browserStackHub == null) {
            throw new RuntimeException("BrowserStack is not initialized. Please check your config.");
        }
        Browser browser = crawlerRequest.getBrowser();
        BrowserResolutionDto browserResolution = crawlerRequest.getBrowserResolution();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browser", browser.getBrowser());
        capabilities.setCapability("browser_version", browser.getBrowserVersion());
        capabilities.setCapability("os", browser.getOs());
        capabilities.setCapability("os_version", browser.getOsVersion());
        capabilities.setCapability("resolution", browserResolution.toString());
        Dimension dimension = new Dimension(browserResolution.getWidth(), browserResolution.getHeight());
        log.info("Connecting to BrowserStack hub using settings: {}.", browser);
        WebDriver driver = new RemoteWebDriver(browserStackHub, capabilities);
        log.info("Connected to BrowserStack hub.");
        driver.manage().window().setSize(dimension);
        return driver;
    }

    public List<Browser> getAllBrowsers() {
        return browserListRetriever.getBrowsers()
                .stream()
                .filter(browser -> !browser.isRealMobile())
                .filter(browser -> !browser.getBrowser().equals("opera")) // Opera is not supported yet
                .collect(toList());
    }

    public Map<String, List<BrowserResolutionDto>> getResolutionsForBrowsers() {
        Map<String, List<BrowserResolutionDto>> resolutions = new HashMap<>();
        List<BrowserResolutionDto> windows = Stream.of(WindowsBrowserResolutionEnum.values())
                .map(BrowserResolutionDto::new)
                .collect(toList());
        List<BrowserResolutionDto> mac = Stream.of(MacBrowserResolutionEnum.values())
                .map(BrowserResolutionDto::new)
                .collect(toList());
        resolutions.put(WindowsBrowserResolutionEnum.OS_NAME, windows);
        resolutions.put(MacBrowserResolutionEnum.OS_NAME, mac);
        return resolutions;
    }

    private URL initializeBrowserStackHubUrl(BrowserStackAuthentication browserStackAuthentication)
            throws MalformedURLException {
        URL browserStackHubUrl = null;
        if (browserStackAuthentication.isCredentialsProvided()) {
            browserStackHubUrl = new URL(String.format(URL, browserStackAuthentication.getUserName(),
                    browserStackAuthentication.getApiKey()));
        }
        return browserStackHubUrl;
    }

}
