import {Component} from '@angular/core';
import {SpinnerService} from "./spinner.service";

@Component({
    selector: 'spinner',
    styleUrls: ['./spinner.component.css'],
    templateUrl: './spinner.component.html'
})

export class SpinnerComponent {
    constructor(public spinnerService: SpinnerService) {
    }
}
