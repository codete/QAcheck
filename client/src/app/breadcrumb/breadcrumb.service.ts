import {Injectable} from "@angular/core";
import {BreadcrumbsConfiguration, IBreadcrumb} from "./breadcrumb.model";
import {Observable, Subject} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class BreadcrumbsService {

    private configuration: BreadcrumbsConfiguration;
    public configurationSource: Subject<BreadcrumbsConfiguration>;
    public configurationChanged$: Observable<BreadcrumbsConfiguration>;

    public constructor() {
        this.configuration = new BreadcrumbsConfiguration();
        this.configurationSource = new Subject<BreadcrumbsConfiguration>();
        this.configurationChanged$ = this.configurationSource.asObservable();
    }

    public setBreadcrumb(label: string, url: string, level: number): void {
        if (level === 0) {
            this.configuration.breadcrumbs = [];
        } else if (this.isForwardBreadcrumb(level)) {
            this.configuration.breadcrumbs.push(this.configuration.currentBreadcrumb);
        } else {
            this.configuration.breadcrumbs = this.filterByLevel(level);
        }

        this.configuration.currentBreadcrumb = {
            label: label,
            url: url,
            level: level
        };

        let config: BreadcrumbsConfiguration = this.configuration;
        this.configurationSource.next(config);
    }

    public get(): Observable<BreadcrumbsConfiguration> {
        return this.configurationChanged$;
    }

    private filterByLevel(level: number): IBreadcrumb[] {
        return this.configuration.breadcrumbs.filter(child => child.level <= level - 1);
    }

    private isForwardBreadcrumb(nextBreadcrumb: number): boolean {
        return this.configuration.currentBreadcrumb && this.configuration.currentBreadcrumb.level < nextBreadcrumb;
    }

}