import {Component, OnInit} from '@angular/core';
import {MatDialog, MatTableDataSource} from '@angular/material';
import {UserAppService} from "../services/user-app.service";
import {UserApp} from "../models/user-app";
import {ConfirmDeleteionDialogComponent} from "../modal-dialogs/confirm-deletion/confirm-deletion.component";

@Component({
    selector: 'user-app-list',
    templateUrl: './user-app-list.component.html',
})
export class UserAppListComponent implements OnInit {
    public userApps: UserApp[];
    public dataSource;
    public filter;
    public displayedColumns: string[] = ['appName', 'action'];

    constructor(private userAppService: UserAppService, public dialog: MatDialog) {
    }

    public ngOnInit() {
        this.userAppService.fetchUserApps().subscribe((apps) => {
            this.userApps = apps;
            this.dataSource = new MatTableDataSource(this.userApps);
        });
    }

    public applyFilter(filterValue: string) {
        this.dataSource.filter = filterValue.trim().toLowerCase();
    }

    public clearFilter() {
        this.applyFilter("");
        this.filter = "";
    }

    public confirmDeletionDialog(userApp: UserApp): void {
        const dialogRef = this.dialog.open(ConfirmDeleteionDialogComponent, {
          width: '250px',
          data: {question: 'Delete application? ', info: '', payload: userApp}
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.deleteUserApp(result);
            }
        });
    }
    public stopPropogation(event) {
        event.stopPropagation();
    }

    public deleteUserApp(userApp: UserApp) {
        this.userAppService.deleteUserApp(userApp.id).subscribe(() => {
            this.userApps = this.userApps.filter((e) => {
                return e !== userApp;
            });
            this.dataSource = new MatTableDataSource(this.userApps);
            this.applyFilter(this.filter);
        });
    }
}
