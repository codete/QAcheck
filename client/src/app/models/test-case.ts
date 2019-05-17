import {Environment} from "./environment";

export class TestCase {
    public uuid: string;
    public userAppId: number;
    public testName: string;
    public passed: boolean;
    public environment: Environment;
    public lastRunTimestamp: number;
}
