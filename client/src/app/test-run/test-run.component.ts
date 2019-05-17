import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {TestStep} from "../models/test-step";
import {TestStepService} from "../services/test-step.service";
import {TestStepStatus} from "../models/test-step-status";

@Component({
    selector: "test-run",
    styleUrls: ["./test-run.component.css"],
    templateUrl: "./test-run.component.html",
})
export class TestRunComponent implements OnInit {
    public testSteps: TestStep[];
    public displayedStep: TestStep;

    public statusPassed = TestStepStatus.PASSED;
    public statusFailed = TestStepStatus.FAILED;
    public statusDecisionRequired = TestStepStatus.DECISION_REQUIRED;

    constructor(private route: ActivatedRoute,
                public testStepService: TestStepService) {
    }

    public ngOnInit() {
        const testRunUuid = this.route.snapshot.paramMap.get("testRunUuid");

        this.testStepService.fetchTestStepsByTestRunUuid(testRunUuid)
            .subscribe((steps) => {
                this.testSteps = steps;
                this.chooseStep(this.testSteps[0]);
            });
    }

    public getColor(step: TestStep) {
        let className: string;
        switch (step.status) {
            case TestStepStatus.PASSED: className = 'test-passed'; break;
            case TestStepStatus.FAILED: className = 'test-failed'; break;
            case TestStepStatus.DECISION_REQUIRED: className = 'test-decision'; break;
        }
        return className;
    }

    public getIcon(step: TestStep) {
        let icon: string;
        switch (step.status) {
            case TestStepStatus.PASSED: icon = 'check_circle_outline'; break;
            case TestStepStatus.FAILED: icon = 'error_outline'; break;
            case TestStepStatus.DECISION_REQUIRED: icon = 'help'; break;
        }
        return icon;
    }

    public chooseStep(step: TestStep): void {
        this.displayedStep = step;
    }
}
