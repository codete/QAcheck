import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {AlertService} from "./alert.service";

@Component({
    selector: 'alert',
    templateUrl: 'alert.component.html'
})

export class AlertComponent implements OnInit, OnDestroy {
    public message: any;
    private subscription: Subscription;

    constructor(private alertService: AlertService) {
    }

    public ngOnInit() {
        this.subscription = this.alertService.getMessage().subscribe((message) => {
            this.message = message;
        });
    }

    public ngOnDestroy() {
        this.subscription.unsubscribe();
    }
}
