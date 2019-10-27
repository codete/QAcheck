import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class BaselineUploadService {

    constructor(private http: HttpClient) {
    }

    public uploadBaseline(testStepId: number, file: File): Observable<any> {
        const formData: FormData = new FormData();
        formData.append('image', file);

        const uploadUrl = `${environment.serverUrl}/test-steps/${testStepId}/upload-baseline`;
        const httpOptions = {
            reportProgress: true,
        };

        return this.http.post(uploadUrl, formData, httpOptions);
    }

}

