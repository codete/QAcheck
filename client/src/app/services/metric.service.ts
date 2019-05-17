import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {GeneralMetric} from '../models/metrics';

@Injectable({
    providedIn: 'root'
})
export class MetricService {
    constructor(private http: HttpClient) {
    }

    public getGeneralMetrics(): Observable<GeneralMetric[]> {
        return this.http.get<GeneralMetric[]>(`${environment.serverUrl}/metrics/app`);
    }
}
