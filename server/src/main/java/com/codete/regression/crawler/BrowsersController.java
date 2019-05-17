package com.codete.regression.crawler;

import com.codete.regression.crawler.browserstack.Browser;
import com.codete.regression.crawler.browserstack.BrowserResolutionDto;
import com.codete.regression.crawler.browserstack.BrowserStackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("browsers")
public class BrowsersController {

    private final BrowserStackService browserStackService;

    public BrowsersController(BrowserStackService browserStackService) {
        this.browserStackService = browserStackService;
    }

    @GetMapping
    public List<Browser> getAllBrowsers() {
        return browserStackService.getAllBrowsers();
    }

    @GetMapping("/resolutions")
    public Map<String, List<BrowserResolutionDto>> getResolutionsForBrowsers() {
        return browserStackService.getResolutionsForBrowsers();
    }
}
