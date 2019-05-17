import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {ComparisonSettings} from "../models/comparison-settings";

@Injectable({
    providedIn: 'root'
})
export class ComparisonService {

    constructor(private http: HttpClient) {
    }

    public saveComparisonSettings(testStepId: number, comparisonSettings: ComparisonSettings): Observable<any> {
        const url = `${environment.serverUrl}/test-steps/${testStepId}/comparison-settings`;
        const httpOptions = {
            headers: new HttpHeaders({'Content-Type': 'application/json'})
        };
        return this.http.put(url, JSON.stringify(comparisonSettings), httpOptions);
    }

    public recompareTestStepScreenshotWithBaseline(testStepId: number): Observable<any> {
        const url = `${environment.serverUrl}/test-steps/${testStepId}/repeat-comparison`;
        return this.http.put(url, {});
    }
}
