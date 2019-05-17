import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {TestCase} from "../models/test-case";
import {environment} from "../../environments/environment";
import {TokenStorageService} from "../authentication/token-storage.service";
import { TestCaseFilters } from "../models/test-case-filters";

@Injectable({
    providedIn: "root",
})
export class TestCaseService {
    constructor(private http: HttpClient, private tokenStorageService: TokenStorageService) {
    }

    public fetchTestCases(userAppName: string, page: number, pageSize: number, filters: TestCaseFilters) {
        const testCaseUrl = `${environment.serverUrl}/test-cases`;
        let params = {
            appName: userAppName,
            username: this.tokenStorageService.getUsername(),
            page: String(page),
            limit: String(pageSize)
        };

        Object.keys(filters).forEach((key) => {
            if (filters[key]) {
                params[`filters.${key}`] = filters[key];
            }
        });

        return this.http.get<TestCase[]>(testCaseUrl, {observe: 'response', params: params});
    }

    public fetchTestCaseByUuid(uuid: string): Observable<TestCase> {
        const testCaseUrl = `${environment.serverUrl}/test-cases/${uuid}`;
        return this.http.get<TestCase>(testCaseUrl);
    }

    public deleteTestCase(testCaseUuid: string) {
        return this.http.delete(`${environment.serverUrl}/test-cases/${testCaseUuid}`);
    }
    public getEnvironments(appName: string) {
        return this.http.get(`${environment.serverUrl}/environments/${appName}/filters`);
    }
}
