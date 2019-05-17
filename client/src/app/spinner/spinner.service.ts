import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class SpinnerService {
    private _loading = false;

    public set loading(value: boolean) {
        this._loading = value;
    }

    public get loading() {
        return this._loading;
    }
}
