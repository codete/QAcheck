package com.codete.regression.crawler;

import com.codete.regression.authentication.exception.UserNotFoundException;
import com.codete.regression.authentication.user.User;
import com.codete.regression.authentication.user.UserService;
import com.codete.regression.crawler.instance.CrawlerInstance;
import com.codete.regression.crawler.instance.CrawlerInstanceDto;
import com.codete.regression.crawler.instance.CrawlerInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("site-map-crawler")
@RequiredArgsConstructor
public class CrawlerController {
    private final UserService userService;
    private final CrawlerTaskExecutor crawlerTaskExecutor;
    private final CrawlerInstanceService crawlerInstanceService;

    @PostMapping("/run")
    public ResponseEntity runCrawler(@RequestBody CrawlerRequest crawlerRequest) {
        String username = userService.getCurrentUser()
                .map(User::getUsername)
                .orElseThrow(UserNotFoundException::new);
        Optional<CrawlerInstance> crawlerInstance = crawlerInstanceService.getActiveInstanceForUsername(username);
        if (crawlerInstance.isPresent()) {
            return new ResponseEntity<>("Another crawler for user is running", HttpStatus.BAD_REQUEST);
        } else {
            crawlerTaskExecutor.runCrawlerTask(username, crawlerRequest);
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/stop")
    public void stopCrawler() {
        String username = userService.getCurrentUser()
                .map(User::getUsername)
                .orElseThrow(UserNotFoundException::new);

        crawlerInstanceService.stopInstanceForUser(username, "Stopping instance...");
    }

    @GetMapping("/status")
    public CrawlerInstanceDto getCrawlerStatus() {
        return userService.getCurrentUser()
                .map(user -> crawlerInstanceService.getActiveInstanceForUsername(user.getUsername())
                        .orElseGet(() -> {
                            CrawlerInstance crawlerInstance = new CrawlerInstance();
                            crawlerInstance.setCurrentLog("Not running");
                            crawlerInstance.setProgress(100.0f);
                            return crawlerInstance;
                        }))
                .map(CrawlerInstanceDto::new)
                .orElseThrow(UserNotFoundException::new);
    }
}
