package com.codete.regression.config;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:db",
})
@ActiveProfiles("test")
public class TestApplicationConfig {

    protected static String API_KEY = "9e1ecdcd-75e7-4a40-9f15-0ec651e813d5";
    protected static String USERNAME = "administrator";
}
