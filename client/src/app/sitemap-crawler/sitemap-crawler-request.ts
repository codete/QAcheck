import {Browser} from "./browser";
import {BrowserResolution} from "./browser-resolution";

export class SitemapCrawlerRequest {
    public sitemapPath: string;
    public appName: string;
    public browser: Browser = new Browser();
    public browserResolution: BrowserResolution = null;
    public detectDynamicElements: boolean;
}
