package com.codete.regression.metrics;

import com.codete.regression.authentication.exception.UserNotFoundException;
import com.codete.regression.authentication.user.User;
import com.codete.regression.authentication.user.UserService;
import com.codete.regression.metrics.data.UserAppMetricsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {
    private final MetricsService metricsService;
    private final UserService userService;

    @GetMapping("/app")
    public List<UserAppMetricsDto> getMetrics() {
        User user = userService.getCurrentUser()
                .orElseThrow(UserNotFoundException::new);

        return metricsService.getUserAppMetrics(user.getId());
    }
}
