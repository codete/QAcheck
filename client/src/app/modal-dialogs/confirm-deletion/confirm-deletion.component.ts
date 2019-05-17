import {Component, Inject} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

export interface IDialogData {
  question: string;
  info: string;
  payload: any;
}

@Component({
    selector: 'confirm-deletion-dialog',
    templateUrl: 'confirm-deletion.component.html',
    styleUrls: ['confirm-deletion.component.css']
  })
  export class ConfirmDeleteionDialogComponent {

    constructor(
      public dialogRef: MatDialogRef<ConfirmDeleteionDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data: IDialogData) {}

    public onNoClick(): void {
      this.dialogRef.close();
    }
}
