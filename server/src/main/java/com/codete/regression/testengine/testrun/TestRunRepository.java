package com.codete.regression.testengine.testrun;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface TestRunRepository extends JpaRepository<TestRun, Long> {

    Page<TestRun> findAllByTestCaseUuid(String uuid, Pageable pageable);

    Optional<TestRun> findByUuid(String uuid);

    Optional<TestRun> findTop1ByTestCaseUuidAndUuidNotOrderByRunTimestampDesc(String testCaseUuid, String testRunUuid);
}
