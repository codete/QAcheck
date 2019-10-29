export interface IBreadcrumb {
    label: string;
    url: string;
    level: number;
}

export class BreadcrumbsConfiguration {
    public currentBreadcrumb: IBreadcrumb;
    public breadcrumbs: IBreadcrumb[] = [];
}