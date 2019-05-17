import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from "../authentication.service";
import {AlertService} from "../alert/alert.service";
import {AuthLoginInfo} from "./login-info";
import {TokenStorageService} from "../token-storage.service";

@Component({
    templateUrl: 'login.component.html'
})
export class LoginComponent implements OnInit {
    public loginForm: FormGroup;
    public loading = false;
    public submitted = false;
    public returnUrl: string;

    constructor(private formBuilder: FormBuilder, private route: ActivatedRoute, private router: Router,
                private alertService: AlertService, private authService: AuthenticationService,
                private tokenStorage: TokenStorageService) {
    }

    public ngOnInit() {
        this.loginForm = this.formBuilder.group({
            password: ['', Validators.required],
            username: ['', Validators.required]
        });
        this.tokenStorage.signOut();
        this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
    }

    get form() {
        return this.loginForm.controls;
    }

    public onSubmit() {
        this.submitted = true;
        if (this.loginForm.invalid) {
            return;
        }
        this.loading = true;
        const loginInfo = new AuthLoginInfo(this.form.username.value, this.form.password.value);
        this.authService.signIn(loginInfo).subscribe(
            (data) => {
                this.tokenStorage.saveToken(data.access_token);
                this.tokenStorage.saveRefreshToken(data.refresh_token);
                this.tokenStorage.saveUsername(loginInfo.username);
                this.router.navigate([this.returnUrl]);
            },
            (error) => {
                this.alertService.error(error.error.error_description);
                this.loading = false;
            }
        );
    }
}
