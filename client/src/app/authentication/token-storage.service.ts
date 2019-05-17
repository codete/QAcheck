import {Subject} from "rxjs";

const TOKEN_KEY = 'AuthToken';
const REFRESH_TOKEN_KEY = 'RefreshToken';
const USERNAME_KEY = 'AuthUsername';

export class TokenStorageService {

    private loginSubject: Subject<boolean> = new Subject();

    public getLoginSubject(): Subject<boolean> {
        return this.loginSubject;
    }

    public signOut() {
        localStorage.clear();
        this.loginSubject.next(false);
    }

    public saveToken(token: string) {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.setItem(TOKEN_KEY, token);
        this.loginSubject.next(true);
    }

    public saveRefreshToken(token: string) {
        localStorage.removeItem(REFRESH_TOKEN_KEY);
        localStorage.setItem(REFRESH_TOKEN_KEY, token);
    }

    public removeToken() {
        localStorage.removeItem(TOKEN_KEY);
    }

    public getToken(): string {
        return localStorage.getItem(TOKEN_KEY);
    }

    public getRefreshToken(): string {
        return localStorage.getItem(REFRESH_TOKEN_KEY);
    }

    public getUsername(): string {
        return localStorage.getItem(USERNAME_KEY);
    }

    public userSettingsChanged() {
        this.loginSubject.next(true);
    }

    public saveUsername(username: string) {
        localStorage.removeItem(USERNAME_KEY);
        localStorage.setItem(USERNAME_KEY, username);
    }
}
