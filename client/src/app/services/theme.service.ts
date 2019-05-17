import {Injectable} from '@angular/core';
import {Observable, BehaviorSubject} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class ThemeService {

    public currentTheme = new BehaviorSubject(environment.defaultTheme);

    public setTheme(themeClass: string) {
        this.currentTheme.next(themeClass);
    }

    public getTheme(): Observable<string> {
        return this.currentTheme.asObservable();
    }
}
