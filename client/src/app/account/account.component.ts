import {Component, OnInit} from "@angular/core";
import { UserService } from "../services/user.service";
import { User } from "../models/user";
import { TokenStorageService } from "../authentication/token-storage.service";

@Component({
    selector: "account",
    templateUrl: "./account.component.html",
})
export class AccountComponent implements OnInit {
    public currentUser: User;
    public loaded = false;
    public dataSource: any[] = [];
    public displayedColumns: string[] = ['property', 'value'];
    public whiteTheme = false;
    private currentSettings: User;

    constructor(private tokenStorageService: TokenStorageService, private userService: UserService) {
    }

    public changeTheme(event: any) {
      this.currentSettings.whiteTheme = event.checked;
      this.userService.saveUserSettings(this.currentSettings).subscribe(
        (result) => {
          this.tokenStorageService.userSettingsChanged();
      });
    }

    public ngOnInit() {
      this.userService.getCurrentUser().subscribe((user) => {
        this.loaded = true;
        this.currentSettings = user;
        this.whiteTheme = user.whiteTheme;
        this.currentUser = user;
        this.dataSource.push({property: 'API Key', value: user.apiKey});
      });
    }
}
