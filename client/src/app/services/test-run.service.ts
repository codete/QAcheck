import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {TestRun} from "../models/test-run";
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class TestRunService {

    constructor(private http: HttpClient) {
    }

    public fetchTestRuns(testCaseUuid: string, page: number, pageLength: number) {
        const testRunUrl = `${environment.serverUrl}/test-runs`;
        return this.http.get<TestRun[]>(testRunUrl, {observe: 'response', params: {
            testCaseUuid: testCaseUuid,
            limit: String(pageLength),
            page: String(page),
        }});
    }

    public deleteTestRun(testRunUuid: string) {
        return this.http.delete(`${environment.serverUrl}/test-runs/${testRunUuid}`);
    }
}
