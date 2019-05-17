import {AfterViewInit, Component, ElementRef, HostListener, Input, OnDestroy, ViewChild} from '@angular/core';
import {fromEvent, Subscription} from "rxjs";
import {tap} from "rxjs/operators";
import * as SVG from "svg.js";
import "svg.draggable.js";
import "svg.select.js";
import "svg.resize.js";
import {IgnoreAreaService} from "./ignore-area.service";
import {IgnoreArea} from "../models/ignore-area";
import {ComparisonSettings} from "../models/comparison-settings";
import {IgnoreAreaDeleteButton} from "./ignore-area-delete-button";

@Component({
    selector: 'ignore-area',
    styleUrls: ['./ignore-area.component.css'],
    templateUrl: './ignore-area.component.html',
})
export class IgnoreAreaComponent implements AfterViewInit, OnDestroy {

    @ViewChild('box')
    public box: ElementRef;
    public compSettings: ComparisonSettings;
    public ignoreAreaDeleteButton: IgnoreAreaDeleteButton = new IgnoreAreaDeleteButton();
    private rectangles: SVG.Rect[] = [];
    private draw: SVG.Doc;
    private originalScreenshotWidth: number;
    private previousBoxWidth: number;
    private originalWidthSubscription: Subscription;
    private addIgnoreAreaSubscription: Subscription;

    constructor(private ignoreAreaService: IgnoreAreaService) {
    }

    @Input()
    public set comparisonSettings(comparisonSettings: ComparisonSettings) {
        this.compSettings = comparisonSettings;
        this.removeRectanglesFocus();
        this.rectangles.forEach((rect) => {
            rect.remove();
        });
    }

    @HostListener("window:resize")
    public onResize() {
        this.removeRectanglesFocus();
        this.rescale();
    }

    public ngAfterViewInit() {
        this.draw = SVG('ignore-area-box');
        // ignore areas should be drawn after view initialized AND background current screenshot loaded
        this.originalWidthSubscription = this.ignoreAreaService.originalWidthSubject
            .subscribe((width: number) => this.drawOnScreenshotLoad(width));
        this.addIgnoreAreaSubscription = this.ignoreAreaService.addIgnoreAreaSubject
            .subscribe(() => this.addRectangle());
        fromEvent(this.box.nativeElement, 'mousedown')
            .pipe(
                tap(() => this.removeRectanglesFocus())
            )
            .subscribe();
    }

    public ngOnDestroy(): void {
        if (this.originalWidthSubscription) {
            this.originalWidthSubscription.unsubscribe();
        }
        if (this.addIgnoreAreaSubscription) {
            this.addIgnoreAreaSubscription.unsubscribe();
        }
    }

    public removeArea() {
        if (confirm("Delete this ignore area?")) {
            this.removeRectangle(this.ignoreAreaDeleteButton.selectedRectangle);
            this.ignoreAreaDeleteButton.hidden = true;
        }
    }

    public getAllIgnoreAreas(): IgnoreArea[] {
        return this.ignoreAreaService.convertRectanglesToIgnoreAreas(
            this.rectangles, this.originalScreenshotWidth / this.box.nativeElement.clientWidth);
    }

    private removeRectanglesFocus(): void {
        this.ignoreAreaDeleteButton.hidden = true;
        this.rectangles.forEach((rect) => {
            if (rect.parents().length) {
                // @ts-ignore
                rect.selectize(false).resize(false).draggable();
            }
        });
    }

    private drawOnScreenshotLoad(originalScreenshotWidth: number): void {
        this.originalScreenshotWidth = originalScreenshotWidth;
        this.previousBoxWidth = this.originalScreenshotWidth;
        this.drawIgnoreAreas();
        this.rescale();
        this.removeRectanglesFocus();
    }

    private drawIgnoreAreas() {
        this.rectangles = this.ignoreAreaService.convertIgnoreAreasToRectangles(this.compSettings.ignoreAreas,
            this.draw);
        this.rectangles.forEach((rect) => this.createDragAndResizeListeners(rect));
    }

    private rescale() {
        if (this.box) {
            this.rectangles.forEach((rect) =>
                this.rescaleRectangle(rect, this.box.nativeElement.clientWidth / this.previousBoxWidth));
            this.previousBoxWidth = this.box.nativeElement.clientWidth;
        }
    }

    private rescaleRectangle(rect: SVG.Rect, scale: number): SVG.Rect {
        rect.x(rect.x() * scale);
        rect.y(rect.y() * scale);
        rect.width(rect.width() * scale);
        rect.height(rect.height() * scale);
        return rect;
    }

    private createDragAndResizeListeners(rectangle: SVG.Rect): void {
        rectangle.on("resizing", () => {
            this.ignoreAreaDeleteButton.hidden = true;
        });
        rectangle.on("resizedone", (event) => {
            const selectedRectangle = this.getRectangleByCoordinates(event.srcElement.x.baseVal.value,
                event.srcElement.y.baseVal.value);
            this.showDeleteButton(selectedRectangle);
        });
        rectangle.on("dragend.namespace", (event) => {
            const selectedRectangle = this.getRectangleByCoordinates(event.detail.p.x, event.detail.p.y);
            if (selectedRectangle) {
                // @ts-ignore
                selectedRectangle.draggable(false).selectize({rotationPoint: false}).resize();
                this.showDeleteButton(selectedRectangle);
            }
        });
    }

    private showDeleteButton(rectangle: SVG.Rect) {
        this.ignoreAreaDeleteButton.positionX = rectangle.x() + rectangle.width() + 3;
        this.ignoreAreaDeleteButton.positionY = rectangle.y();
        this.ignoreAreaDeleteButton.selectedRectangle = rectangle;
        this.ignoreAreaDeleteButton.hidden = false;
    }

    private getRectangleByCoordinates(x: number, y: number): SVG.Rect | null {
        for (const rect of this.rectangles) {
            if (rect.x() <= x
                && rect.x() + rect.width() >= x
                && rect.y() <= y
                && rect.y() + rect.height() >= y) {
                return rect;
            }
        }
        return null;
    }

    private removeRectangle(rect: SVG.Rect): void {
        if (rect) {
            const index = this.rectangles.indexOf(rect);
            if (index === -1) {
                return;
            }
            // @ts-ignore
            this.rectangles[index].selectize(false).resize(false).remove();
            this.rectangles.splice(index, 1);
        }
    }

    private addRectangle(): void {
        const rect = this.draw.rect(50, 50).stroke(IgnoreAreaService.STROKE_COLOR)
        // @ts-ignore
            .fill(IgnoreAreaService.FILL_COLOR).draggable();
        this.createDragAndResizeListeners(rect);
        this.rectangles.push(rect);
    }

}
