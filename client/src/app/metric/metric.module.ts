import {CommonModule} from '@angular/common';
import {NgModule} from "@angular/core";
import {GeneralMetricComponent} from "./general-metric.component";
import {MatTabsModule} from '@angular/material/tabs';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatCardModule} from '@angular/material/card';
import {PieChartModule} from '@swimlane/ngx-charts';
import {MatButtonModule} from '@angular/material/button';
import {RouterModule} from "@angular/router";

@NgModule({
    declarations: [
        GeneralMetricComponent
    ],
    imports: [
        CommonModule,
        RouterModule,
        MatTabsModule,
        MatGridListModule,
        MatButtonModule,
        PieChartModule,
        MatCardModule
    ],
    exports: [
        GeneralMetricComponent
    ]
})
export class MetricModule {
}
