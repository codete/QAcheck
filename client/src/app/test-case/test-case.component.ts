import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {TestRun} from "../models/test-run";
import {TestRunService} from "../services/test-run.service";
import {TestCaseService} from "../services/test-case.service";
import {TestCase} from "../models/test-case";
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {ConfirmDeleteionDialogComponent} from "../modal-dialogs/confirm-deletion/confirm-deletion.component";
import {PageEvent} from '@angular/material';

@Component({
    selector: "test-case",
    styleUrls: ["./test-case.component.css"],
    templateUrl: "./test-case.component.html",
})
export class TestCaseComponent implements OnInit {
    public testCase: TestCase;
    public appName: string;
    public testRuns: TestRun[] = [];
    public displayedColumns: string[] = ['runTimestamp', 'action'];
    public currentPage = 0;
    public totalPages = 1;

    public totalLength = 100;
    public pageSize = 10;
    public pageSizeOptions: number[] = [5, 10, 25, 100];

    public pageEvent: PageEvent;

    constructor(public testRunService: TestRunService,
                private testCaseService: TestCaseService,
                private route: ActivatedRoute,
                public dialog: MatDialog) {
    }

    public ngOnInit() {
        this.appName = this.route.snapshot.paramMap.get("appName");
        const testCaseUuid = this.route.snapshot.paramMap.get("testCaseUuid");
        this.testCaseService.fetchTestCaseByUuid(testCaseUuid).subscribe((testCase) => this.testCase = testCase);
        this.fetchTestSteps(testCaseUuid);
    }

    public confirmDeletionDialog(testRun: TestRun): void {
        const dialogRef = this.dialog.open(ConfirmDeleteionDialogComponent, {
          width: '250px',
          data: {question: 'Delete test run? ', info: '', payload: testRun}
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.deleteTestRun(result);
            }
        });
    }

    public deleteTestRun(testRun: TestRun) {
        this.testRunService.deleteTestRun(testRun.uuid).subscribe(() => {
            this.testRuns = this.testRuns.filter((e) => {
                return e !== testRun;
            });
        });
    }

    public stopPropogation(event) {
        event.stopPropagation();
    }

    public changePage(event) {
        this.pageSize = event.pageSize;
        this.currentPage = event.pageIndex;
        this.fetchTestSteps(this.testCase.uuid);
    }

    private fetchTestSteps(testCaseUuid) {
        this.testRunService.fetchTestRuns(testCaseUuid, this.currentPage, this.pageSize).subscribe((resp) => {
            this.testRuns = resp.body;
            this.totalLength = parseInt(resp.headers.get('X-Total-Count'), 10);
            this.totalPages = parseInt(resp.headers.get('X-Total-Pages'), 10);
        });
    }
}
