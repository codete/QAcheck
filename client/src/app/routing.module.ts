import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {TestCaseListComponent} from "./test-case-list/test-case-list.component";
import {TestCaseComponent} from "./test-case/test-case.component";
import {TestRunComponent} from "./test-run/test-run.component";
import {UserAppListComponent} from "./user-app-list/user-app-list.component";
import {SitemapCrawlerComponent} from "./sitemap-crawler/sitemap-crawler.component";
import {AccountComponent} from "./account/account.component";
import {AuthGuard} from "./authentication/auth.guard";
import {DocumentationComponent} from "./documentation/documentation.component";
import {GeneralMetricComponent} from "./metric/general-metric.component";

const routes: Routes = [
    {path: "documentation", component: DocumentationComponent, pathMatch: "full", data: { breadcrumb: "Documentation", level: 0}},
    {path: "documentation/:id", component: DocumentationComponent, pathMatch: "full", data: { dynamicBreadcrumb: "id", level: 1}},
    {
        path: '', canActivate: [AuthGuard], children: [
            {path: "metrics", component: GeneralMetricComponent, pathMatch: "full", data: { breadcrumb: "Metrics", level: 0}},
            {path: "account", component: AccountComponent, pathMatch: "full", data: { breadcrumb: "Account", level: 0}},
            {path: "test-runs/:appName/:testRunUuid", component: TestRunComponent, pathMatch: "full", data: { breadcrumb: "Test Run", level: 3}},
            {path: "test-case/:appName/:testCaseUuid", component: TestCaseComponent, pathMatch: "full"},
            {path: "crawler-runner", component: SitemapCrawlerComponent, pathMatch: "full"},
            {path: "test-cases/:appName/:pageSize/:currentPage", component: TestCaseListComponent, pathMatch: "full", data: { dynamicBreadcrumb: "appName", level: 1}},
            {path: "test-cases/:appName/:pageSize/:currentPage/:testName/:os/:browser/:viewPortWidth/:viewPortHeight/:passed/:lastRunTimestampFrom/:lastRunTimestampTo", component: TestCaseListComponent, pathMatch: "full", data: { dynamicBreadcrumb: "appName", level: 1}},
            {path: "", component: UserAppListComponent, pathMatch: "full", data: { breadcrumb: "Applications", level: 0}},
        ]
    }
];

@NgModule({
    exports: [RouterModule],
    imports: [RouterModule.forRoot(routes)],
})
export class RoutingModule {
}
