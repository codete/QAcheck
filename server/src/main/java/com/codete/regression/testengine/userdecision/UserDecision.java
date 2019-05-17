package com.codete.regression.testengine.userdecision;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.stream.Stream;

@Getter
@Setter
@Entity
@Table(name = "user_decision")
public class UserDecision {

    @Id
    private int id;

    @Enumerated(EnumType.STRING)
    private UserDecisionEnum value;

    public enum UserDecisionEnum {
        NO_DECISION(0), BASELINE_ACCEPTED(1), CURRENT_ACCEPTED(2);

        private final int numberRepresentation;

        UserDecisionEnum(int numberRepresentation) {
            this.numberRepresentation = numberRepresentation;
        }

        public int getNumberRepresentation() {
            return numberRepresentation;
        }

        public static UserDecisionEnum getUserDecisionForNumber(int numberRepresentation) {
            return Stream.of(UserDecisionEnum.values())
                    .filter(userDecisionEnum -> userDecisionEnum.numberRepresentation == numberRepresentation)
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find User decision for number="
                            + numberRepresentation));
        }
    }
}
