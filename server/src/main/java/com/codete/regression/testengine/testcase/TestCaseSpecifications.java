package com.codete.regression.testengine.testcase;

import com.codete.regression.authentication.user.User;
import com.codete.regression.authentication.user.User_;
import com.codete.regression.testengine.environment.Environment;
import com.codete.regression.testengine.environment.Environment_;
import com.codete.regression.testengine.testcase.TestCase_;
import com.codete.regression.testengine.testcase.request.TestCaseFilters;
import com.codete.regression.testengine.testrun.TestRun;
import com.codete.regression.testengine.testrun.TestRun_;
import com.codete.regression.testengine.userapp.UserApp;
import com.codete.regression.testengine.userapp.UserApp_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestCaseSpecifications {

    public static Specification<TestCase> byUsernameAndAppName(String username, String appName) {
        return (Specification<TestCase>) (root, query, cb) -> {
            Join<TestCase, UserApp> userApp = root.join(TestCase_.userApp);
            Join<UserApp, User> user = userApp.join(UserApp_.user);
            return cb.and(
                    cb.equal(userApp.get(UserApp_.appName), appName),
                    cb.equal(user.get(User_.username), username)
            );
        };
    }

    public static Specification<TestCase> byFilters(TestCaseFilters filters) {
        return (Specification<TestCase>) (root, query, cb) -> {
            Join<TestCase, Environment> environment = root.join(TestCase_.environment);
            Join<TestCase, TestRun> latestTestRun = root.join(TestCase_.latestTestRun, JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (filters.getTestName() != null) {
                predicates.add(cb.like(root.get(TestCase_.testName), "%" + filters.getTestName() + "%"));
            }

            if (filters.getBrowser() != null) {
                predicates.add(cb.equal(environment.get(Environment_.browser), filters.getBrowser()));
            }

            if (filters.getOs() != null) {
                predicates.add(cb.equal(environment.get(Environment_.os), filters.getOs()));
            }

            if (filters.getViewPortWidth() != null) {
                predicates.add(cb.equal(environment.get(Environment_.viewPortWidth), filters.getViewPortWidth()));
            }

            if (filters.getViewPortHeight() != null) {
                predicates.add(cb.equal(environment.get(Environment_.viewPortHeight), filters.getViewPortHeight()));
            }

            if (filters.getPassed() != null) {
                predicates.add(cb.equal(latestTestRun.get(TestRun_.passed), filters.getPassed()));
            }

            if (filters.getLastRunTimestampFrom() != null) {
                Date runDateFrom = new Date(filters.getLastRunTimestampFrom());
                predicates.add(cb.greaterThanOrEqualTo(latestTestRun.get(TestRun_.runTimestamp), runDateFrom));
            }

            if (filters.getLastRunTimestampTo() != null) {
                Date runDateTo = new Date(filters.getLastRunTimestampTo());
                predicates.add(cb.lessThanOrEqualTo(latestTestRun.get(TestRun_.runTimestamp), runDateTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
