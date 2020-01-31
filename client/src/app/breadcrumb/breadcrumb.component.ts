import {Component, Input, OnInit} from "@angular/core";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {filter} from 'rxjs/operators';
import {BreadcrumbsConfiguration, IBreadcrumb} from "./breadcrumb.model";
import {BreadcrumbsService} from "./breadcrumb.service";

@Component({
    selector: "breadcrumb",
    templateUrl: "./breadcrumb.component.html"
})
export class BreadcrumbComponent implements OnInit {
    private ROUTE_DATA_BREADCRUMB: string = "breadcrumb";
    private ROUTE_DATA_DYNAMIC_BREADCRUMB: string = "dynamicBreadcrumb";
    private ROUTE_DATA_LEVEL: string = "level";

    public breadcrumbConfiguration: BreadcrumbsConfiguration;

    constructor(private breadcrumbsService: BreadcrumbsService, private activatedRoute: ActivatedRoute, private router: Router) {
        this.breadcrumbsService.get().subscribe((configuration: BreadcrumbsConfiguration) => {
            this.breadcrumbConfiguration = configuration as BreadcrumbsConfiguration;
        });
    }

    ngOnInit(): void {
        if (this.router.navigated) {
            this.generateBreadcrumbTrail();
        }

        this.router.events.pipe(
            filter(event => event instanceof NavigationEnd)
        ).subscribe(event => {
            this.generateBreadcrumbTrail();
        });
    }

    private generateBreadcrumbTrail(): void {
        const currentRoute = this.activatedRoute.root;

        let activatedRoutes = this.flatChildren(currentRoute.children)
            .filter(route => (route.routeConfig && route.routeConfig.data));
        if (activatedRoutes.length > 0) {
            let route = activatedRoutes[0];

            const data = route.snapshot.data;
            const breadcrumbLevel: number = data.hasOwnProperty(this.ROUTE_DATA_LEVEL) ? data[this.ROUTE_DATA_LEVEL] : 0;
            let breadcrumbLabel: string = this.calculateLabel(data, route);
            let breadcrumbUrl: string = this.router.url;

            this.breadcrumbsService.setBreadcrumb(breadcrumbLabel, breadcrumbUrl, breadcrumbLevel);
        }
    }

    private flatChildren(routes: ActivatedRoute[]): ActivatedRoute[] {
        return routes.reduce((flat, route) => flat.concat(route.children), []);
    }

    private calculateLabel(data: any, route: ActivatedRoute): string {
        const label = data[this.ROUTE_DATA_BREADCRUMB];
        if (label) {
            return label;
        }
        const paramName = data[this.ROUTE_DATA_DYNAMIC_BREADCRUMB];
        return route.snapshot.params[paramName];
    }

}