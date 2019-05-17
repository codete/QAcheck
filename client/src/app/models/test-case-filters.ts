export class TestCaseFilters {
  public testName: string = null;
  public os: string = null;
  public browser: string = null;
  public viewPortWidth: number = null;
  public viewPortHeight: number = null;
  public passed: boolean = null;
  public lastRunTimestampFrom: number = null;
  public lastRunTimestampTo: number = null;

  public isEmpty() {
    return Object.keys(this).filter((key) => (this[key] !== null)).length === 0;
  }

  public setValues(filterValues: any) {
    this.testName = filterValues.testName;
    this.os = filterValues.os;
    this.browser = filterValues.browser;
    this.viewPortWidth = filterValues.viewPortWidth;
    this.viewPortHeight = filterValues.viewPortHeight;
    this.passed = filterValues.passed;
    this.lastRunTimestampFrom = filterValues.lastRunTimestampFrom ? new Date(filterValues.lastRunTimestampFrom).getTime() : null;
    this.lastRunTimestampTo = filterValues.lastRunTimestampTo ? new Date(filterValues.lastRunTimestampTo).getTime() : null;
  }

  public getFilterForRoute() {
    const list = [this.testName, this.os, this.browser, this.viewPortWidth, this.viewPortHeight,
      this.passed, this.lastRunTimestampFrom, this.lastRunTimestampTo];
    return list.map((value) => value ? value : '-').join('/');
  }
}
