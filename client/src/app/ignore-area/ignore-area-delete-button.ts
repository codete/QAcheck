import * as SVG from "svg.js";

export class IgnoreAreaDeleteButton {
    public positionX: number;
    public positionY: number;
    public hidden: boolean;
    public selectedRectangle: SVG.Rect;

    public constructor() {
        this.positionX = 0;
        this.positionY = 0;
        this.hidden = true;
    }
}
