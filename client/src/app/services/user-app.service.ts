import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {UserApp} from "../models/user-app";
import {TokenStorageService} from "../authentication/token-storage.service";

@Injectable({
    providedIn: 'root'
})
export class UserAppService {
    constructor(private http: HttpClient, private tokenStorageService: TokenStorageService) {
    }

    public fetchUserApps(): Observable<UserApp[]> {
        const userAppsUrl = `${environment.serverUrl}/user-app?username=${this.tokenStorageService.getUsername()}`;
        return this.http.get<UserApp[]>(userAppsUrl);
    }

    public deleteUserApp(id: number) {
        return this.http.delete(`${environment.serverUrl}/user-app/${id}`);
    }
}
