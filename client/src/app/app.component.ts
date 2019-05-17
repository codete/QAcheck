import {Component, ViewChild, HostBinding} from "@angular/core";
import {TokenStorageService} from "./authentication/token-storage.service";
import {UserService} from "./services/user.service";
import {ThemeService} from "./services/theme.service";

@Component({
    selector: "app-root",
    templateUrl: "./app.component.html",
    host: {
        class: 'mat-app-background'
    }
})
export class AppComponent {
    public title = "regression-testing-platform";
    public isAthenticated = false;
    public menuExpanded = false;
    @HostBinding('class.dark-theme') private darkTheme = true;
    @HostBinding('class.light-theme') private lightTheme = false;

    constructor(private tokenStorage: TokenStorageService,
                private themeService: ThemeService,
                private userService: UserService) {
    }

    public toggleMenu() {
        this.menuExpanded = !this.menuExpanded;
    }

    private getUserProfile() {
        if (this.isAthenticated) {
            this.userService.getCurrentUser().subscribe(
                (userData) => {
                    this.darkTheme = !userData.whiteTheme;
                    this.lightTheme = userData.whiteTheme;
                    this.themeService.setTheme(userData.whiteTheme ? 'light-theme' : 'dark-theme');
                }
            );
        }
    }

    private ngOnInit() {
        this.isAthenticated = !!this.tokenStorage.getToken();
        this.getUserProfile();

        this.tokenStorage.getLoginSubject().subscribe(
            (isAthenticated) => {
                this.isAthenticated = isAthenticated;
                this.getUserProfile();
            }
        );
    }

}
