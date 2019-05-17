package com.codete.regression.crawler.instance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrawlerInstanceRepository extends JpaRepository<CrawlerInstance, Long> {

    Optional<CrawlerInstance> findByUserUsername(String username);
}
