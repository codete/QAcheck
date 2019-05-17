import {Component, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {MetricService} from '../services/metric.service';
import {GeneralMetric} from '../models/metrics';
import {colorScheme} from './colors';
import { ThemeService } from '../services/theme.service';

@Component({
    selector: 'general-metric',
    templateUrl: 'general-metric.component.html'
})
export class GeneralMetricComponent implements OnInit {

    public colorScheme = colorScheme.dark;
    public generalMetrics: GeneralMetric[] = [];

    constructor(private metricService: MetricService, private themeService: ThemeService) {
    }

    public ngOnInit() {
        this.themeService.getTheme().subscribe((themeClass) => {
            this.colorScheme = themeClass === "dark-theme" ? colorScheme.dark : colorScheme.light;
        });
        this.metricService.getGeneralMetrics().subscribe(
            (metrics) => {
                this.generalMetrics = metrics.map((app) => {
                    app.data = [];
                    app.lastRunDate = new Date(app.lastRunDate).toLocaleString();
                    app.data.push({name: 'successful test cases', value: app.casesSuccessCount});
                    app.data.push({name: 'failed test cases', value: app.casesFailsCount});
                    return app;
                 });
            }
        );
    }

}
