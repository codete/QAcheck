import {IgnoreArea} from "./ignore-area";

export class ComparisonSettings {
    public allowedDifferencePercentage: number;
    public allowedDelta: number;
    public horizontalShift: number;
    public verticalShift: number;
    public showDetectedShift: boolean;
    public perceptualMode: boolean;
    public ignoreAreas: IgnoreArea[];
}
