package com.codete.regression.crawler.instance;

import com.codete.regression.authentication.exception.UserNotFoundException;
import com.codete.regression.authentication.user.User;
import com.codete.regression.authentication.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CrawlerInstanceService {

    private static final int MAX_INACTIVE_UPDATE_TIME_IN_MINUTES = 3;
    private final CrawlerInstanceRepository crawlerInstanceRepository;
    private final UserService userService;

    public Optional<CrawlerInstance> getActiveInstanceForUsername(String username) {
        LocalDateTime nowMinusIdleTime = LocalDateTime.now(Clock.systemUTC())
                .minusMinutes(MAX_INACTIVE_UPDATE_TIME_IN_MINUTES);
        return crawlerInstanceRepository.findByUserUsername(username)
                .filter(crawlerInstance -> nowMinusIdleTime.isBefore(crawlerInstance.getUpdateTimestamp()));
    }

    public boolean updateIfNotStopped(Long instanceId, String currentLog, float progress) {
        Optional<CrawlerInstance> crawlerInstance = crawlerInstanceRepository.findById(instanceId);
        if (!crawlerInstance.isPresent()) {
            return false;
        }

        CrawlerInstance instance = crawlerInstance.get();
        if (instance.isForceStop()) {
            return false;
        }

        update(instance, currentLog, progress);
        return true;
    }

    public void delete(CrawlerInstance crawlerInstance) {
        crawlerInstanceRepository.delete(crawlerInstance);
    }

    public CrawlerInstance startNewInstanceForUser(String username, String currentLog) {
        CrawlerInstance crawlerInstance = findByUserOrCreateNewOne(username);
        update(crawlerInstance, currentLog, 0.0f);
        return crawlerInstance;
    }

    public void stopInstanceForUser(String username, String currentLog) {
        CrawlerInstance crawlerInstance = findByUserOrCreateNewOne(username);
        crawlerInstance.setUpdateTimestamp(LocalDateTime.now(Clock.systemUTC()));
        crawlerInstance.setCurrentLog(currentLog);
        crawlerInstance.setForceStop(true);
        crawlerInstanceRepository.save(crawlerInstance);
    }

    private void update(CrawlerInstance crawlerInstance, String currentLog, float progress) {
        crawlerInstance.setUpdateTimestamp(LocalDateTime.now(Clock.systemUTC()));
        crawlerInstance.setCurrentLog(currentLog);
        crawlerInstance.setProgress(progress);
        crawlerInstanceRepository.save(crawlerInstance);
    }

    private CrawlerInstance findByUserOrCreateNewOne(String username) {
        return crawlerInstanceRepository.findByUserUsername(username)
                .orElseGet(() -> {
                    User user = userService.findByUsername(username).orElseThrow(UserNotFoundException::new);
                    return crawlerInstanceRepository.save(new CrawlerInstance(user, LocalDateTime.now(Clock.systemUTC())));
                });
    }


}
