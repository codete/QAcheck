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
    {path: "documentation", component: DocumentationComponent, pathMatch: "full"},
    {path: "documentation/:id", component: DocumentationComponent, pathMatch: "full"},
    {
        path: '', canActivate: [AuthGuard], children: [
            {path: "metrics", component: GeneralMetricComponent, pathMatch: "full"},
            {path: "account", component: AccountComponent, pathMatch: "full"},
            {path: "test-runs/:appName/:testRunUuid", component: TestRunComponent, pathMatch: "full"},
            {path: "test-case/:appName/:testCaseUuid", component: TestCaseComponent, pathMatch: "full"},
            {path: "crawler-runner", component: SitemapCrawlerComponent, pathMatch: "full"},
            {path: "test-cases/:appName/:pageSize/:currentPage", component: TestCaseListComponent, pathMatch: "full"},
            {path: "test-cases/:appName/:pageSize/:currentPage/:testName/:os/:browser/:viewPortWidth/:viewPortHeight/:passed/:lastRunTimestampFrom/:lastRunTimestampTo", component: TestCaseListComponent, pathMatch: "full"},
            {path: "", component: UserAppListComponent, pathMatch: "full"},
        ]
    }
];

@NgModule({
    exports: [RouterModule],
    imports: [RouterModule.forRoot(routes)],
})
export class RoutingModule {
}
