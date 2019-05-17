import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import { User } from '../models/user';

@Injectable({
    providedIn: 'root'
})
export class UserService {
    constructor(private http: HttpClient) {
    }

    public getCurrentUser(): Observable<User> {
        return this.http.get<User>(`${environment.serverUrl}/users/me`);
    }

    public saveUserSettings(user: User): Observable<any> {
        return this.http.put<any>(`${environment.serverUrl}/users/me/save`, user);
    }
}
