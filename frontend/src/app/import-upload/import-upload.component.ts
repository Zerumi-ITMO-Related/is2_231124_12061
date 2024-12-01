import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FileUploadService } from '../file-upload.service';
import { HttpClient } from '@angular/common/http';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { HumanBeing } from '../model';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { DialogRef } from '@angular/cdk/dialog';

@Component({
  selector: 'app-import-upload',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    MatCheckboxModule,
    MatRadioModule,
    MatSelectModule,
    MatIconModule,
    CommonModule,
  ],
  templateUrl: './import-upload.component.html',
  styleUrl: './import-upload.component.css',
})
export class ImportUploadComponent implements OnInit {
  newModelForm!: FormGroup;
  invitation!: string;

  currentFile?: File;
  message = '';
  fileInfos?: Observable<any>;

  constructor(
    private uploadService: FileUploadService,
    private fb: FormBuilder,
    private dialogRef: DialogRef<ImportUploadComponent>
  ) {
    this.invitation = 'File uploading';
  }

  ngOnInit(): void {
    this.newModelForm = this.fb.group({
      name: ['', Validators.required],
      x: ['', [Validators.required, Validators.max(605)]],
      y: ['', [Validators.required]],
      realHero: [false],
      hasToothpick: [false],
      carName: [null],
      mood: ['', Validators.required],
      impactSpeed: ['', [Validators.required]],
      minutesOfWaiting: ['', [Validators.required]],
      weaponType: ['', Validators.required],
    });
  }

  selectFile(event: any): void {
    this.currentFile = event.target.files.item(0);
  }

  onCancel() {
    this.dialogRef.close();
  }

  onSubmit() {
    this.upload();
  }

  upload(): void {
    if (this.currentFile) {
      this.uploadService.upload(this.currentFile).subscribe({
        next: (event: any) => {
          if (event instanceof HttpResponse) {
            this.dialogRef.close();
            alert('Successfully imported ' + event.body + ' models!');
          }
        },
        error: (err: any) => {
          console.log(err);

          if (err.error && err.error.message) {
            this.message = err.error.message;
          } else {
            this.message = 'Could not upload the file!';
          }
        },
        complete: () => {
          this.currentFile = undefined;
        },
      });
    }
  }
}
