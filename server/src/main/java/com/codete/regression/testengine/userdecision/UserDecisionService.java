package com.codete.regression.testengine.userdecision;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDecisionService {

    private final UserDecisionRepository userDecisionRepository;

    public UserDecisionService(UserDecisionRepository userDecisionRepository) {
        this.userDecisionRepository = userDecisionRepository;
    }

    @Transactional(readOnly = true)
    public UserDecision findByValue(UserDecision.UserDecisionEnum userDecisionEnum) {
        return userDecisionRepository.findByValue(userDecisionEnum);
    }
}
