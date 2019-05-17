import {environment} from '../environments/environment';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {AppComponent} from "./app.component";
import {RoutingModule} from "./routing.module";
import {TestStepComponent} from "./test-step/test-step.component";
import {TestCaseListComponent} from "./test-case-list/test-case-list.component";
import {TestCaseComponent} from "./test-case/test-case.component";
import {TestRunComponent} from "./test-run/test-run.component";
import {SitemapCrawlerComponent} from "./sitemap-crawler/sitemap-crawler.component";
import {ReactiveFormsModule, FormsModule} from '@angular/forms';
import {NgxSpinnerModule} from 'ngx-spinner';
import {UserAppListComponent} from './user-app-list/user-app-list.component';
import {IgnoreAreaComponent} from './ignore-area/ignore-area.component';
import {AccountComponent} from './account/account.component';
import {AuthenticationModule} from "./authentication/authentication.module";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SpinnerInterceptor} from "./spinner/spinner-interceptor.service";
import {SpinnerComponent} from "./spinner/spinner.component";
import {DocumentationModule} from "./documentation/documentation.module";
import {MetricModule} from "./metric/metric.module";

import {MenuModule} from "./menu/menu.module";

import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatListModule} from '@angular/material/list';
import {MatIconModule} from '@angular/material/icon';
import {MatTabsModule} from '@angular/material/tabs';
import {MatCardModule} from '@angular/material/card';
import {MatTableModule} from '@angular/material/table';
import {MatMenuModule} from '@angular/material/menu';
import {MatInputModule} from '@angular/material/input';
import {MatDialogModule} from '@angular/material/dialog';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatSelectModule} from '@angular/material/select';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatChipsModule} from '@angular/material/chips';
import {MatSnackBarModule, MAT_SNACK_BAR_DEFAULT_OPTIONS} from '@angular/material/snack-bar';
import {MatSliderModule} from '@angular/material/slider';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatMomentDateModule} from '@angular/material-moment-adapter';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';

import {MAT_MOMENT_DATE_FORMATS, MomentDateAdapter} from '@angular/material-moment-adapter';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';

import {ConfirmDeleteionDialogComponent} from './modal-dialogs';
import {AdvancedSettingsDialogComponent} from './modal-dialogs';

import {OverlayContainer} from '@angular/cdk/overlay';

import * as _moment from 'moment';
import { ThemeService } from './services/theme.service';

@NgModule({
    bootstrap: [AppComponent],
    declarations: [
        AppComponent,
        TestStepComponent,
        TestCaseListComponent,
        TestCaseComponent,
        TestRunComponent,
        SitemapCrawlerComponent,
        UserAppListComponent,
        IgnoreAreaComponent,
        AccountComponent,
        SpinnerComponent,
        ConfirmDeleteionDialogComponent,
        AdvancedSettingsDialogComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        MenuModule,
        DocumentationModule,
        RoutingModule,
        AuthenticationModule,
        MetricModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        NgxSpinnerModule,
        MatSidenavModule,
        MatToolbarModule,
        MatButtonModule,
        MatListModule,
        MatIconModule,
        MatTabsModule,
        MatCardModule,
        MatTableModule,
        MatMenuModule,
        MatInputModule,
        MatDialogModule,
        MatCheckboxModule,
        MatSelectModule,
        MatGridListModule,
        MatSlideToggleModule,
        MatChipsModule,
        MatSnackBarModule,
        MatSliderModule,
        MatPaginatorModule,
        MatMomentDateModule,
        MatDatepickerModule,
        MatProgressSpinnerModule
    ],
    entryComponents: [
        ConfirmDeleteionDialogComponent,
        AdvancedSettingsDialogComponent
    ],
    exports: [
        ConfirmDeleteionDialogComponent,
        AdvancedSettingsDialogComponent
    ],
    providers: [
        {provide: HTTP_INTERCEPTORS, useClass: SpinnerInterceptor, multi: true},
        {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
        {provide: MAT_DATE_FORMATS, useValue: environment.dateFormats},
        {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {verticalPosition: 'top'}}
    ],
})
export class AppModule {
    private lastThemeClass: string;
    constructor(private overlayContainer: OverlayContainer, private themeService: ThemeService) {
        themeService.getTheme().subscribe((themeClass) => {
            this.overlayContainer.getContainerElement().classList.remove(this.lastThemeClass);
            this.overlayContainer.getContainerElement().classList.add(themeClass);
            this.lastThemeClass = themeClass;
        });
    }
}
