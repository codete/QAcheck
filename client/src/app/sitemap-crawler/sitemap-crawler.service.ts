import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {SitemapCrawlerRequest} from "./sitemap-crawler-request";
import {CrawlerInstance} from "./crawler-instance";

@Injectable({
    providedIn: "root",
})
export class SitemapCrawlerService {

    private static SITEMAP_CRAWLER_URL = `${environment.serverUrl}/site-map-crawler/`;

    constructor(private http: HttpClient) {
    }

    public runSitemapCrawler(sitemapCrawlerRequest: SitemapCrawlerRequest): Observable<any> {
        const httpOptions = {
            headers: new HttpHeaders({'Content-Type': 'application/json'})
        };
        return this.http.post(SitemapCrawlerService.SITEMAP_CRAWLER_URL + 'run', sitemapCrawlerRequest, httpOptions);
    }

    public getCrawlerStatus(): Observable<CrawlerInstance> {
        return this.http.get<CrawlerInstance>(SitemapCrawlerService.SITEMAP_CRAWLER_URL + 'status');
    }
}
