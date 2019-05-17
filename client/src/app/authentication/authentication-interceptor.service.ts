import {HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {TokenStorageService} from './token-storage.service';
import {AuthenticationService} from "./authentication.service";
import {BehaviorSubject, throwError} from 'rxjs';
import {catchError, filter, finalize, switchMap, take} from 'rxjs/operators';
import {Router} from '@angular/router';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {

    private refreshTokenInProgress: boolean = false;
    private tokenSubject: BehaviorSubject<string> = new BehaviorSubject<string>(null);

    constructor(private tokenStorageService: TokenStorageService, private authService: AuthenticationService,
                private router: Router) {
    }

    public intercept(req: HttpRequest<any>, next: HttpHandler): any {
        const authReq = this.addTokenHeader(req, this.tokenStorageService.getToken());
        return next.handle(authReq)
            .pipe(catchError((error) => {
                if (error instanceof HttpErrorResponse) {
                    return this.handleHttpError(error, req, next);
                } else {
                    return throwError(error);
                }
            }));
    }

    private addTokenHeader(req: HttpRequest<any>, token: string) {
        let httpRequest = req;
        if (token !== null) {
            httpRequest = req.clone({headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token)});
        }
        return httpRequest;
    }

    private handleHttpError(error: HttpErrorResponse, req: HttpRequest<any>, next: HttpHandler) {
        if (req.body && typeof req.body === "string" && req.body.startsWith("refresh_token")) {
            this.logoutUser();
            return throwError(error);
        } else {
            switch (error.status) {
                case 401:
                    return this.handle401Error(req, next);
                case 403:
                    this.router.navigate(['/forbidden']);
                    return throwError(error);
                default:
                    return throwError(error);
            }
        }
    }

    private logoutUser() {
        this.router.navigate(['/login']);
    }

    private handle401Error(req: HttpRequest<any>, next: HttpHandler) {
        if (!this.refreshTokenInProgress) {
            this.refreshTokenInProgress = true;
            this.tokenSubject.next(null);
            this.tokenStorageService.removeToken();
            return this.authService.refreshToken()
                .pipe(
                    switchMap((data: any) => {
                        this.tokenStorageService.saveToken(data.access_token);
                        this.tokenStorageService.saveRefreshToken(data.refresh_token);
                        this.tokenSubject.next(data.access_token);
                        return next.handle(this.addTokenHeader(req, data.access_token));
                    }),
                    finalize(() => {
                        this.refreshTokenInProgress = false;
                    }));
        } else {
            return this.tokenSubject.pipe(
                filter((token) => token != null),
                take(1),
                switchMap((token) => {
                    return next.handle(this.addTokenHeader(req, token));
                }));
        }
    }

}
