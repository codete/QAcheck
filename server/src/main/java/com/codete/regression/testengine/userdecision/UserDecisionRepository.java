package com.codete.regression.testengine.userdecision;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserDecisionRepository extends JpaRepository<UserDecision, Integer> {

    UserDecision findByValue(UserDecision.UserDecisionEnum userDecisionEnum);
}
