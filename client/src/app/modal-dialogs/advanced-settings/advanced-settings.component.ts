import {Component, Inject, Input, Output, EventEmitter} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

import {ComparisonSettings} from "../../models/comparison-settings";

export interface IDialogData {
  settings: ComparisonSettings;
}

@Component({
  selector: 'advanced-settings',
  templateUrl: './advanced-settings.component.html',
  styleUrls: ['./advanced-settings.component.css']
})
export class AdvancedSettingsDialogComponent {
  public settings: ComparisonSettings;

  constructor(public dialogRef: MatDialogRef<AdvancedSettingsDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: IDialogData) {
    this.settings = Object.assign(new ComparisonSettings(), data.settings);
  }

  public onCancelClick(): void {
    this.dialogRef.close();
  }
}
