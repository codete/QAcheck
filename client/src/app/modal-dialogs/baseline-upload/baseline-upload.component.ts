import {Component} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material';

@Component({
    selector: 'baseline-upload-dialog',
    templateUrl: 'baseline-upload.component.html',
    styleUrls: ['baseline-upload.component.css']
  })
  export class BaselineUploadDialogComponent {

    public selectedFile: File;

    constructor(public dialogRef: MatDialogRef<BaselineUploadDialogComponent>) {
    }

    public selectFile(event) {
        this.selectedFile = event.target.files[0];
    }

    public onNoClick(): void {
      this.dialogRef.close();
    }
}
