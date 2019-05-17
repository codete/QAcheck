import { Component, ElementRef, Input, ViewChild } from "@angular/core";
import { UserDecision } from "../models/user-decision";
import { TestStep } from "../models/test-step";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import { SafeResourceUrl } from "@angular/platform-browser";
import { ScreenshotService } from "./screenshot.service";
import { UserDecisionService } from "./user-decision.service";
import { IgnoreAreaComponent } from "../ignore-area/ignore-area.component";
import { ComparisonService } from "./comparison.service";
import { ComparisonSettings } from "../models/comparison-settings";
import { IgnoreAreaService } from "../ignore-area/ignore-area.service";
import { TestStepService } from "../services/test-step.service";
import { TestStepStatus } from "../models/test-step-status";
import { MatSnackBar } from '@angular/material';
import { AdvancedSettingsDialogComponent } from "../modal-dialogs";

@Component({
    selector: "test-step",
    styleUrls: ["./test-step.component.scss"],
    templateUrl: "./test-step.component.html",
})
export class TestStepComponent {

    @ViewChild(IgnoreAreaComponent)
    public ignoreArea: IgnoreAreaComponent;
    public step: TestStep;
    public comparisonSettings: ComparisonSettings;
    public showingDiff: boolean = false;
    public baseline: SafeResourceUrl;
    public current: SafeResourceUrl;
    public diff: SafeResourceUrl;
    @ViewChild('cur')
    public currentImage: ElementRef;
    public recompareButtonEnabled: boolean = true;

    constructor(
        private userService: UserDecisionService,
        private matSnackBar: MatSnackBar,
        private dialog: MatDialog,
        private screenshotService: ScreenshotService,
        private comparisonService: ComparisonService,
        private ignoreAreaService: IgnoreAreaService,
        private testStepService: TestStepService) {
    }

    @Input()
    public set setStep(step: TestStep) {
        this.step = step;
        this.fetchDetails();
    }

    public publishOriginalWidth() {
        this.ignoreAreaService.originalWidthSubject.next(this.currentImage.nativeElement.naturalWidth);
    }

    public acceptBaseline() {
        this.step.userDecision = UserDecision.BASELINE_ACCEPTED;
        this.userService.sendUserDecision(this.step.id, UserDecision.BASELINE_ACCEPTED).subscribe();
        this.step = this.userService.populateStatus(this.step);
    }

    public acceptCurrent() {
        this.step.userDecision = UserDecision.CURRENT_ACCEPTED;
        this.userService.sendUserDecision(this.step.id, UserDecision.CURRENT_ACCEPTED).subscribe();
        this.step = this.userService.populateStatus(this.step);
    }

    public addIgnoreArea() {
        this.ignoreAreaService.addIgnoreAreaSubject.next();
    }

    public async saveSettingsAndRecompare() {
        this.recompareButtonEnabled = false;
        try {
            await this.saveComparisonSettings();
            this.comparisonService.recompareTestStepScreenshotWithBaseline(this.step.id).subscribe((passed) => {
                this.step.status = (passed) ? TestStepStatus.PASSED : TestStepStatus.FAILED;
                this.fetchDetails();
                this.recompareButtonEnabled = true;
            }, () => {
                this.recompareButtonEnabled = true;
            });
        } catch (e) {
            this.recompareButtonEnabled = true;
        }
    }

    public advancedSettingsDialog(): void {
        const dialogRef = this.dialog.open(AdvancedSettingsDialogComponent, {
          width: '550px',
          data: { settings: this.comparisonSettings}
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.comparisonSettings = result;
                this.saveSettingsAndRecompare();
            }
        });
    }

    private saveComparisonSettings() {
        return new Promise((resolve, reject) => {
            this.comparisonSettings.ignoreAreas = this.ignoreArea.getAllIgnoreAreas();
            this.comparisonService.saveComparisonSettings(this.step.id, this.comparisonSettings)
                .subscribe(() => {
                    this.matSnackBar.open('Comparison settings successfully saved', '', {
                        duration: 2 * 1000
                    });
                    resolve();
                }, () => {
                    this.matSnackBar.open('Comparison settings were not saved due to validation' +
                        'error or problem on server', 'OK');
                    reject();
                });
        });
    }

    private fetchDetails() {
        if (this.step) {
            this.testStepService.fetchTestStepDetails(this.step.id).subscribe(
                (details) => {
                    this.comparisonSettings = details.comparisonSettings;
                    this.baseline = this.screenshotService.parseImage(details.screenshots.baselineScreenshot);
                    this.current = this.screenshotService.parseImage(details.screenshots.currentScreenshot);
                    this.diff = this.screenshotService.parseImage(details.screenshots.diffScreenshot);
                    this.showingDiff = details.screenshots.diffScreenshot !== null;
                });
        }
    }
}
