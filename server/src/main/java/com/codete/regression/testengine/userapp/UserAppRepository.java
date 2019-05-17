package com.codete.regression.testengine.userapp;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface UserAppRepository extends JpaRepository<UserApp, Long> {

    @EntityGraph(value = "UserApp.testCases", type = EntityGraph.EntityGraphType.LOAD)
    Optional<UserApp> findByUserUsernameAndAppName(String userName, String appName);

    List<UserApp> findAllByUserUsername(String userName);
}
