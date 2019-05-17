import {UserDecision} from "./user-decision";
import {TestStepStatus} from "./test-step-status";

export class TestStep {

    public id: number;
    public stepName: string;
    public passed: boolean;
    public userDecision: UserDecision;
    // does not present on server, calculated on site
    public status: TestStepStatus;
}
