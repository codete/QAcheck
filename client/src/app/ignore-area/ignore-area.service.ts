import {Injectable} from '@angular/core';
import * as SVG from "svg.js";
import {IgnoreArea} from "../models/ignore-area";
import {Subject} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class IgnoreAreaService {

    public static STROKE_COLOR: string = '#ff0c11';
    public static FILL_COLOR: {} = {color: '#9a9a9a', opacity: 0.6};
    public originalWidthSubject: Subject<number>;
    public addIgnoreAreaSubject: Subject<null>;

    constructor() {
        this.originalWidthSubject = new Subject<number>();
        this.addIgnoreAreaSubject = new Subject<null>();
    }

    public convertIgnoreAreasToRectangles(ignoreAreas: IgnoreArea[], parent: SVG.Doc): SVG.Rect[] {
        return ignoreAreas.map((area) => {
            return parent.rect(area.width, area.height).stroke(IgnoreAreaService.STROKE_COLOR)
                .fill(IgnoreAreaService.FILL_COLOR).move(area.x, area.y);
        });
    }

    public convertRectanglesToIgnoreAreas(rectangles: SVG.Rect[], scale: number): IgnoreArea[] {
        return rectangles.map((rect) => {
            return new IgnoreArea(
                rect.x() * scale,
                rect.y() * scale,
                rect.width() * scale,
                rect.height() * scale,
            );
        });
    }
}
