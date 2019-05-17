import {Injectable} from '@angular/core';
import {UserDecision} from "../models/user-decision";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {TestStep} from "../models/test-step";
import {TestStepStatus} from "../models/test-step-status";

@Injectable({
    providedIn: 'root'
})
export class UserDecisionService {

    constructor(private http: HttpClient) {
    }

    public sendUserDecision(testStepId: number, userDecision: UserDecision): Observable<any> {
        const testRunUrl = `${environment.serverUrl}/test-steps/${testStepId}/user-decision`;
        const httpOptions = {
            headers: new HttpHeaders({'Content-Type': 'application/json'})
        };
        return this.http.put(testRunUrl, userDecision, httpOptions);
    }

    public populateStatuses(testSteps: TestStep[]): TestStep[] {
        if (!testSteps) {
            return testSteps;
        }
        testSteps.forEach((testStep) => this.populateStatus(testStep));
        return testSteps;
    }

    public populateStatus(testStep: TestStep): TestStep {
        if (testStep.userDecision === UserDecision.CURRENT_ACCEPTED ||
            (testStep.userDecision === UserDecision.NO_DECISION && testStep.passed)) {
            testStep.status = TestStepStatus.PASSED;
        } else if (testStep.userDecision === UserDecision.BASELINE_ACCEPTED) {
            testStep.status = TestStepStatus.FAILED;
        } else {
            testStep.status = TestStepStatus.FAILED;
        }
        return testStep;
    }
}
