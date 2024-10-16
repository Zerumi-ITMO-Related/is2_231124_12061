import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
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
import { CommonModule } from '@angular/common';
import { DialogRef } from '@angular/cdk/dialog';

@Component({
  selector: 'app-new-model',
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
  templateUrl: './new-model.component.html',
  styleUrl: './new-model.component.css',
})
export class NewModelComponent implements OnInit {
  newModelForm!: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient, private dialogRef: DialogRef<NewModelComponent>) {}

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
      minutesOfWaiting: [
        '',
        [Validators.required],
      ],
      weaponType: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.newModelForm.valid) {
      console.log('New model form submitted');
      console.log(this.newModelForm.value);
      const newModel = this.newModelForm.value;
      this.newModelForm.reset();
      const newHumanBeing = new HumanBeing();
      newHumanBeing.name = newModel.name;
      newHumanBeing.coordinates = { x: newModel.x, y: newModel.y };
      newHumanBeing.realHero = newModel.realHero;
      newHumanBeing.hasToothpick = newModel.hasToothpick;
      newHumanBeing.car = { name: newModel.carName };
      newHumanBeing.mood = newModel.mood;
      newHumanBeing.impactSpeed = newModel.impactSpeed;
      newHumanBeing.minutesOfWaiting = newModel.minutesOfWaiting;
      newHumanBeing.weaponType = newModel.weaponType;
      this.http
        .post('http://localhost:8080/api/v1/model', newHumanBeing)
        .subscribe();
      this.dialogRef.close();
    } else {
      console.log('New model form invalid');
      console.log(this.getFormValidationErrors());
    }
  }

  getFormValidationErrors() {
    Object.keys(this.newModelForm.controls).forEach((key) => {
      const controlErrors: ValidationErrors =
        this.newModelForm.get(key)?.errors ?? [];
      if (controlErrors != null) {
        Object.keys(controlErrors).forEach((keyError) => {
          console.log(
            'Key control: ' + key + ', keyError: ' + keyError + ', err value: ',
            controlErrors[keyError]
          );
        });
      }
    });
  }
}
