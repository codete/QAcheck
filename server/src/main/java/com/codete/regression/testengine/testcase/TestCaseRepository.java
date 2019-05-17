package com.codete.regression.testengine.testcase;

import com.codete.regression.testengine.userapp.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

interface TestCaseRepository extends JpaRepository<TestCase, Long>, JpaSpecificationExecutor<TestCase> {

    Optional<TestCase> findByUserAppAndTestNameAndEnvironment_Id(UserApp userApp, String testName,
                                                                 long environmentId);

    Optional<TestCase> findByUuid(String uuid);

}
