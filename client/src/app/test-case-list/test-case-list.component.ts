import {Component, OnInit} from "@angular/core";
import {TestCase} from "../models/test-case";
import {TestCaseService} from "../services/test-case.service";
import {Router, ActivatedRoute, NavigationStart, NavigationEnd} from "@angular/router";
import {FormBuilder, FormGroup, FormControl} from '@angular/forms';
import {TestCaseFilters} from "../models/test-case-filters";
import {BrowserStackService} from "../sitemap-crawler/browser-stack.service";
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {ConfirmDeleteionDialogComponent} from "../modal-dialogs/confirm-deletion/confirm-deletion.component";
import {MAT_DATE_FORMATS} from '@angular/material/core';

import * as _moment from 'moment';

@Component({
    selector: "test-case-list",
    templateUrl: "./test-case-list.component.html",
    styleUrls: ["./test-case-list.component.css"]
})
export class TestCaseListComponent implements OnInit {
    public appName;
    public testCases: TestCase[] = [];
    public displayedColumns: string[] = ['result', 'testName', 'os', 'browser',
        'viewPortWidth', 'viewPortHeight', 'lastRunTimestamp', 'action'];
    public currentPage = 0;
    public totalPages = 1;
    public filters: TestCaseFilters = new TestCaseFilters();

    public totalLength = 100;
    public pageSize = 10;
    public pageSizeOptions: number[] = [5, 10, 25, 100];

    public operatingSystems: any[];
    public browserTypes: any[];
    public viewPortHeights: any[] = [];
    public viewPortWidths: any[] = [];
    public filtersForm: FormGroup;
    public resultOptions = [ { title: 'Passed', value: 'true'}, { title: 'Failed', value: 'false'}];

    constructor(public testCaseService: TestCaseService,
                private route: ActivatedRoute,
                private router: Router,
                private formBuilder: FormBuilder,
                private browserStackService: BrowserStackService,
                public dialog: MatDialog) {
    }

    public prepareFilterForm() {
        this.filtersForm = this.formBuilder.group({
            testName: [''],
            os: [''],
            browser: [''],
            viewPortWidth: [''],
            viewPortHeight: [''],
            passed: [''],
            lastRunTimestampFrom: [''],
            lastRunTimestampTo: ['']
        });
    }

    public updateFilterValues(valuesFromRouter: any) {
        const values: any = {};
        Object.assign(values, valuesFromRouter);

        Object.keys(values).forEach((key) => {
            if (values[key] === '-') {
                values[key] = '';
            }
        });

        if (values.lastRunTimestampFrom) {
            values.lastRunTimestampFrom = _moment.unix(values.lastRunTimestampFrom / 1000);
        }

        if (values.lastRunTimestampTo) {
            values.lastRunTimestampTo = _moment.unix(values.lastRunTimestampTo / 1000);
        }
        this.filtersForm.patchValue(values);
    }

    public ngOnInit(): void {
        this.appName = this.route.snapshot.paramMap.get("appName");

        this.prepareFilterForm();

        // track back/forward
        this.router.events.forEach((event) => {
            if (event instanceof NavigationEnd) {
                const pageChanged = this.restorePageState();
                const filterChanged = this.restoreFilterState();

                this.fetchTestCases();
            }
        });

        this.restorePageState();
        this.restoreFilterState();

        this.fetchFilterSelectLists();
        this.fetchTestCases();
    }

    public checkSum(a: number, b: number) {
        return a * b + b;
    }

    public restorePageState() {
        const checkSum = this.checkSum(this.pageSize, this.currentPage);

        const pageSize = this.route.snapshot.paramMap.get("pageSize");
        if (pageSize) {
            this.pageSize = parseInt(pageSize, 10);
        }

        const currentPage = this.route.snapshot.paramMap.get("currentPage");
        if (currentPage) {
            this.currentPage = parseInt(currentPage, 10);
        }
        return checkSum !== this.checkSum(this.pageSize, this.currentPage);
    }
    /**
     * Restore filter state from route
     * return true if filter was updated
     */
    public restoreFilterState() {
        const prevState = this.filtersForm.value;
        this.updateFilterValues((this.route.snapshot.paramMap as any).params);
        const newState = this.filtersForm.value;

        // check if filter state was changed
        return Object.keys(prevState).some((key) => prevState[key] !== newState[key]);
    }

    public fetchFilterSelectLists() {
        this.testCaseService.getEnvironments(this.appName).subscribe(
            (options: any) => {
                this.browserTypes = options.browsers;
                this.operatingSystems = options.os;
                this.viewPortHeights = options.viewPortHeights.map((v) => String(v));
                this.viewPortWidths = options.viewPortWidths.map((v) => String(v));
            }
        );
    }

    public confirmDeletionDialog(testCase: TestCase): void {
        const dialogRef = this.dialog.open(ConfirmDeleteionDialogComponent, {
          width: '250px',
          data: {question: 'Delete test case? ', info: '', payload: testCase}
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.deleteTestCase(result);
            }
        });
    }

    public stopPropogation(event) {
        event.stopPropagation();
    }

    public deleteTestCase(testCase: TestCase) {
        this.testCaseService.deleteTestCase(testCase.uuid).subscribe(() => {
            this.testCases = this.testCases.filter((e) => {
                return e !== testCase;
            });
        });
    }

    public updateData() {
        this.navigate();
    }

    public changePage(event) {
        this.pageSize = event.pageSize;
        this.currentPage = event.pageIndex;
        this.navigate();
    }

    get testName() {
        return this.filtersForm.get('testName').value;
    }

    public clearTestName() {
        this.filtersForm.get('testName').setValue('');
        this.updateData();
    }

    private navigate() {
         this.filters.setValues(this.filtersForm.value);
        if (this.filters.isEmpty()) {
            this.router.navigate([`/test-cases/${this.appName}/${this.pageSize}/${this.currentPage}/`]);
        } else {
            this.router.navigate([`/test-cases/${this.appName}/${this.pageSize}/${this.currentPage}/${this.filters.getFilterForRoute()}`]);
        }
    }

    private fetchTestCases() {
        this.filters.setValues(this.filtersForm.value);
        this.testCaseService.fetchTestCases(this.appName, this.currentPage, this.pageSize, this.filters).subscribe((resp) => {
            this.testCases = resp.body;
            this.totalLength = parseInt(resp.headers.get('X-Total-Count'), 10);
            this.totalPages = parseInt(resp.headers.get('X-Total-Pages'), 10);
        });
    }

}
