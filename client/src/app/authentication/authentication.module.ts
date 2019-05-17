import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AuthenticationRoutingModule} from './authentication.routing.module';
import {AuthenticationService} from "./authentication.service";
import {AuthenticationInterceptor} from "./authentication-interceptor.service";
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {TokenStorageService} from "./token-storage.service";
import {AuthGuard} from "./auth.guard";
import {AlertComponent} from "./alert/alert.component";
import {AlertService} from "./alert/alert.service";
import {ForbiddenResourceComponent} from "./forbidden-resource/forbidden-resource.component";
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';

@NgModule({
    declarations: [
        AlertComponent,
        LoginComponent,
        RegisterComponent,
        ForbiddenResourceComponent,
    ],
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        HttpClientModule,
        AuthenticationRoutingModule,
        MatIconModule,
        MatButtonModule,
        MatCardModule,
        MatInputModule
    ],
    exports: [
        AlertComponent,
    ],
    providers: [
        AuthGuard,
        AlertService,
        AuthenticationService,
        TokenStorageService,
        {provide: HTTP_INTERCEPTORS, useClass: AuthenticationInterceptor, multi: true}
    ],

})

export class AuthenticationModule {

}
