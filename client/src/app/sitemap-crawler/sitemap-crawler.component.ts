import {Component} from "@angular/core";
import {SitemapCrawlerService} from "./sitemap-crawler.service";
import {SitemapCrawlerRequest} from "./sitemap-crawler-request";
import {NgxSpinnerService} from 'ngx-spinner';
import {Browser} from "./browser";
import {BrowserStackService} from "./browser-stack.service";
import {BrowserResolution} from "./browser-resolution";
import {CrawlerInstance} from "./crawler-instance";
import {Subscription, timer} from "rxjs";
import {take} from "rxjs/operators";

@Component({
    selector: "sitemap-crawler",
    templateUrl: "sitemap-crawler.component.html",
})
export class SitemapCrawlerComponent {

    public operatingSystems = [];
    public osVersions = [];
    public browserVersions = [];
    public browsers = [];
    public browserResolutions = [];
    public sitemapCrawlerRequest: SitemapCrawlerRequest = new SitemapCrawlerRequest();
    public browserStackBrowser: Browser = this.sitemapCrawlerRequest.browser;
    public testFinished: boolean;
    public errorHappened: boolean = false;
    public crawlerInstance: CrawlerInstance = new CrawlerInstance();
    private crawlerInstanceTimerSubscription: Subscription;

    private browserStackEnvironments: Browser[];
    private browserStackResolutionsForOs: Map<string, BrowserResolution[]>;

    constructor(private sitemapCrawlerService: SitemapCrawlerService, private browserStackService: BrowserStackService,
                private spinner: NgxSpinnerService) {
    }

    public ngOnInit() {
        this.browserStackService.getBrowsers().subscribe((browsers) => {
            this.browserStackEnvironments = browsers;
            this.operatingSystems = [...Array.from(new Set(this.browserStackEnvironments
                .map((browser) => browser.os)))];
        });
        this.browserStackService.getBrowserResolutions().subscribe((browserResolutionsForOs) =>
            this.browserStackResolutionsForOs = browserResolutionsForOs);
        this.sitemapCrawlerService.getCrawlerStatus().subscribe((crawlerInstance) => {
            if (crawlerInstance.active) {
                this.spinner.show();
                this.crawlerInstance = crawlerInstance;
                this.subscribeToCrawlerInstanceInfo();
            }
        });
    }

    public ngOnDestroy() {
        if (this.crawlerInstanceTimerSubscription) {
            this.crawlerInstanceTimerSubscription.unsubscribe();
        }
    }

    public runSitemapCrawler() {
        this.spinner.show();
        this.sitemapCrawlerService.runSitemapCrawler(this.sitemapCrawlerRequest).subscribe(
            () => {
                this.crawlerInstance = new CrawlerInstance();
                this.crawlerInstance.active = true;
                this.crawlerInstance.currentLog = "Configuring crawler.";
                this.crawlerInstance.progress = 0;
                this.subscribeToCrawlerInstanceInfo();
            },
            () => {
                this.testFinished = false;
                this.spinner.hide();
                this.errorHappened = true;
                setTimeout(() => this.errorHappened = false, 3000);
            });
    }

    public onSelectOs(os: string) {
        console.log(os);
        this.browserStackBrowser.osVersion = null;
        this.browserStackBrowser.browser = null;
        this.browserStackBrowser.browserVersion = null;
        this.browserVersions = [];
        this.browsers = [];
        this.osVersions = [...Array.from(new Set(this.getFilteredBrowserStackEnvironments()
            .map((browser) => browser.osVersion)))];
        this.browserResolutions = this.browserStackResolutionsForOs.get(this.browserStackBrowser.os.toLowerCase());
    }

    public onSelectOsVersion(osVersion: string) {
        this.browserStackBrowser.browser = null;
        this.browserStackBrowser.browserVersion = null;
        this.browserVersions = [];
        this.browsers = [...Array.from(new Set(this.getFilteredBrowserStackEnvironments()
            .map((browser) => browser.browser)))];
    }

    public onSelectBrowser(selectedBrowser: string) {
        this.browserStackBrowser.browserVersion = null;
        this.browserVersions = [...Array.from(new Set(this.getFilteredBrowserStackEnvironments()
            .map((browser) => browser.browserVersion)))];
    }

    private getFilteredBrowserStackEnvironments(): Browser[] {
        return this.browserStackEnvironments.filter((environment) => {
            const osFilter = this.browserStackBrowser.os && environment.os === this.browserStackBrowser.os;
            const osVersionFilter = !this.browserStackBrowser.osVersion
                || environment.osVersion === this.browserStackBrowser.osVersion;
            const browserFilter = !this.browserStackBrowser.browser
                || environment.browser === this.browserStackBrowser.browser;
            return osFilter && osVersionFilter && browserFilter;
        });
    }

    private subscribeToCrawlerInstanceInfo(): void {
        this.crawlerInstanceTimerSubscription = timer(5000).pipe(take(1)).subscribe(
            () => this.retrieveCrawlerInstanceInfo());
    }

    private retrieveCrawlerInstanceInfo(): void {
        this.sitemapCrawlerService.getCrawlerStatus().subscribe((crawlerInstance) => {
                this.crawlerInstance = crawlerInstance;
                if (!crawlerInstance.active) {
                    this.spinner.hide();
                    this.testFinished = true;
                    this.crawlerInstanceTimerSubscription.unsubscribe();
                } else {
                    this.subscribeToCrawlerInstanceInfo();
                }
            },
            () => {
                this.testFinished = false;
                this.spinner.hide();
                this.errorHappened = true;
                setTimeout(() => this.errorHappened = false, 3000);
            });
    }

}
