import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {AuthLoginInfo} from './login/login-info';
import {SignUpInfo} from './register/signup-info';
import {environment} from "../../environments/environment";
import {TokenStorageService} from "./token-storage.service";

const securityHeaders = {
    'Authorization': 'Basic cmVncmVzc2lvbi10ZXN0aW5nLXBsYXRmb3JtOm15U2VjcmV0UmVncmVzc2lvbg==',
    'Content-type': 'application/x-www-form-urlencoded'
};

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    private tokenUrl = `${environment.serverUrl}/oauth/token`;
    private signupUrl = `${environment.serverUrl}/api/auth/signup`;

    constructor(private http: HttpClient, private tokenStorageService: TokenStorageService) {
    }

    public refreshToken(): Observable<string> {
        const body = new URLSearchParams();
        body.set('refresh_token', this.tokenStorageService.getRefreshToken());
        body.set('grant_type', "refresh_token");
        return this.http.post<any>(this.tokenUrl, body.toString(), {headers: securityHeaders});
    }

    public signIn(credentials: AuthLoginInfo): Observable<any> {
        const body = new URLSearchParams();
        body.set('username', credentials.username);
        body.set('password', credentials.password);
        body.set('grant_type', "password");
        return this.http.post<any>(this.tokenUrl, body.toString(), {headers: securityHeaders});
    }

    public signUp(info: SignUpInfo): Observable<string> {
        const headers = new HttpHeaders({'Content-Type': 'application/json'});
        return this.http.post(this.signupUrl, info, {headers, responseType: 'text'});
    }
}
