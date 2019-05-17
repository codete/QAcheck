package com.codete.regression.testengine.testgroup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface TestGroupRepository extends JpaRepository<TestGroup, Long> {
    Optional<TestGroup> findByUuid(String uuid);
}
