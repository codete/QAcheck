import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatSnackBar } from '@angular/material';

import {AuthenticationService} from "../authentication.service";
import {SignUpInfo} from "./signup-info";
import {AlertService} from "../alert/alert.service";
import {MustMatch} from "./must-match.validator";

@Component({templateUrl: 'register.component.html'})
export class RegisterComponent implements OnInit {
    public registerForm: FormGroup;
    public loading = false;
    public submitted = false;

    constructor(private formBuilder: FormBuilder, private matSnackBar: MatSnackBar,
                private router: Router, private alertService: AlertService,
                private authService: AuthenticationService) {
    }

    public ngOnInit() {
        this.registerForm = this.formBuilder.group({
            firstName: ['', [Validators.required, Validators.minLength(3),
                Validators.pattern('^[a-zA-Z]*$')]],
            lastName: ['', [Validators.required, Validators.minLength(3),
                Validators.pattern('^[a-zA-Z]*$')]],
            username: ['', [Validators.required, Validators.minLength(6),
                Validators.pattern('^[a-zA-Z0-9]*$')]],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(6)]],
            confirmPassword: ['', []],
        }, {validator: MustMatch('password', 'confirmPassword')});
    }

    get form() {
        return this.registerForm.controls;
    }

    public onSubmit() {
        this.submitted = true;
        if (this.registerForm.invalid) {
            return;
        }
        const signupInfo = new SignUpInfo(this.form.firstName.value, this.form.lastName.value,
            this.form.username.value, this.form.email.value, this.form.password.value);

        this.authService.signUp(signupInfo).subscribe(
            (data) => {
                this.matSnackBar.open('Registration successful. Your apiKey: ' + data
                + ' please save it and use it with platform api library.', 'OK');
                this.router.navigate(['/login']);
            },
            (error) => {
                if (error.error.message) {
                    this.alertService.error(error.error.message);
                } else {
                    this.alertService.error(error.error);
                }
                this.loading = false;
            });
    }
}
