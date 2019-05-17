import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {TestStep} from "../models/test-step";
import {environment} from "../../environments/environment";
import {UserDecisionService} from "../test-step/user-decision.service";
import {map} from "rxjs/operators";
import {TestStepDetails} from "../models/test-step-details";

@Injectable({
    providedIn: 'root'
})
export class TestStepService {

    constructor(private http: HttpClient,
                private userDecisionService: UserDecisionService) {
    }

    public fetchTestStepsByTestRunUuid(testRunUuid: string): Observable<TestStep[]> {
        const testRunUrl = `${environment.serverUrl}/test-steps?testRunUuid=${testRunUuid}`;
        return this.http.get<TestStep[]>(testRunUrl)
            .pipe(
                map((steps: TestStep[]) => this.userDecisionService.populateStatuses(steps))
            );
    }

    public fetchTestStepDetails(testStepId: number): Observable<TestStepDetails> {
        const url = `${environment.serverUrl}/test-steps/${testStepId}/details`;
        return this.http.get<TestStepDetails>(url);
    }
}
