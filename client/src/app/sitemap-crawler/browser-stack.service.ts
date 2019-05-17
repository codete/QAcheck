import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {Browser} from "./browser";
import {BrowserResolution} from "./browser-resolution";
import {map} from "rxjs/operators";

@Injectable({
    providedIn: "root",
})
export class BrowserStackService {

    private static BROWSERS_ENDPOINT_URL = `${environment.serverUrl}/browsers`;
    private static BROWSERS_RESOLUTION_ENDPOINT_URL = `${BrowserStackService.BROWSERS_ENDPOINT_URL}/resolutions`;

    constructor(private http: HttpClient) {
    }

    public getBrowsers(): Observable<Browser[]> {
        return this.http.get<Browser[]>(BrowserStackService.BROWSERS_ENDPOINT_URL);
    }

    public getBrowserResolutions(): Observable<Map<string, BrowserResolution[]>> {
        return this.http.get <Map<string, BrowserResolution[]>>(BrowserStackService.BROWSERS_RESOLUTION_ENDPOINT_URL)
            .pipe(
                map((json) => this.buildMap(json)
                ));
    }

    private buildMap(obj): Map<string, BrowserResolution[]> {
        const result = new Map<string, BrowserResolution[]>();
        Object.keys(obj).forEach((key) => {
            result.set(key, obj[key]);
        });
        return result;
    }
}
