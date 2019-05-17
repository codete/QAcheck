import {Injectable} from '@angular/core';
import {DomSanitizer, SafeResourceUrl} from "@angular/platform-browser";

@Injectable({
    providedIn: 'root'
})
export class ScreenshotService {

    constructor(private domSanitizer: DomSanitizer) {
    }

    public parseImage(base64: string): SafeResourceUrl | null {
        return base64 ? this.domSanitizer.bypassSecurityTrustResourceUrl('data:image/png;base64,' + base64) : null;
    }
}
