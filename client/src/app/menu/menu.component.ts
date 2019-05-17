import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {TokenStorageService} from "../authentication/token-storage.service";

@Component({
    selector: "menu",
    styleUrls: ['./menu.component.css'],
    templateUrl: 'menu.component.html'
})
export class MenuComponent implements OnInit, OnDestroy {
    public isLoggedIn: boolean;
    private loginSubscription: Subscription;

    constructor(private route: ActivatedRoute, private router: Router, private tokenStorage: TokenStorageService) {
    }

    public ngOnInit() {
        this.isLoggedIn = this.tokenStorage.getToken() !== null;
        this.loginSubscription =
            this.tokenStorage.getLoginSubject().subscribe((isLoggedIn) => this.isLoggedIn = isLoggedIn);
    }

    public ngOnDestroy() {
        if (this.loginSubscription) {
            this.loginSubscription.unsubscribe();
        }
    }

}
