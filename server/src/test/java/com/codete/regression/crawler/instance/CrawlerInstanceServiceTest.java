package com.codete.regression.crawler.instance;

import com.codete.regression.authentication.user.User;
import com.codete.regression.authentication.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrawlerInstanceServiceTest {

    private final CrawlerInstanceRepository crawlerInstanceRepository = mock(CrawlerInstanceRepository.class);
    private final UserService userService = mock(UserService.class);

    @InjectMocks
    private CrawlerInstanceService crawlerInstanceService;

    @Test
    void shouldCreateNewUserInstanceWhenItDoesNotExist() {
        String username = "TestUser";
        when(userService.findByUsername(username)).thenReturn(Optional.of(mock(User.class)));
        when(crawlerInstanceRepository.save(any())).thenAnswer(returnsFirstArg());
        CrawlerInstance crawlerInstance =
                crawlerInstanceService.startNewInstanceForUser(username, "TestLog");

        verify(crawlerInstanceRepository, times(2)).save(any());
        assertThat(crawlerInstance.getProgress(), is(0.0F));
    }

    @Test
    void shouldUseExistingCrawlerInstanceWhenItExists() {
        String username = "TestUser";
        CrawlerInstance repositoryCrawlerInstance = mock(CrawlerInstance.class);
        when(crawlerInstanceRepository.findByUserUsername(username)).thenReturn(Optional.of(repositoryCrawlerInstance));
        CrawlerInstance crawlerInstance =
                crawlerInstanceService.startNewInstanceForUser(username, "TestLog");

        verify(crawlerInstanceRepository, times(1)).save(any());
        assertThat(crawlerInstance, is(repositoryCrawlerInstance));
    }

}