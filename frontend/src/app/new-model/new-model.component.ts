import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
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
  invitation: string;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private dialogRef: DialogRef<NewModelComponent>,
    @Inject(MAT_DIALOG_DATA)
    public data: { update: boolean; sourceItem: HumanBeing }
  ) {
    if (this.data.update) {
      this.invitation = 'Edit model';
    } else {
      this.invitation = 'Add new model';
    }
  }

  ngOnInit(): void {
    if (this.data.update) {
      this.newModelForm = this.fb.group({
        name: [this.data.sourceItem.name, Validators.required],
        x: [
          this.data.sourceItem.coordinates.x,
          [Validators.required, Validators.max(605)],
        ],
        y: [this.data.sourceItem.coordinates.y, [Validators.required]],
        realHero: [this.data.sourceItem.realHero],
        hasToothpick: [this.data.sourceItem.hasToothpick],
        carName: [this.data.sourceItem.car?.name],
        mood: [this.data.sourceItem.mood, Validators.required],
        impactSpeed: [this.data.sourceItem.impactSpeed, [Validators.required]],
        minutesOfWaiting: [
          this.data.sourceItem.minutesOfWaiting,
          [Validators.required],
        ],
        weaponType: [this.data.sourceItem.weaponType, Validators.required],
      });
    } else {
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
      console.log(newHumanBeing);
      if (this.data.update) {
        this.http
          .put('http://localhost:8080/api/v1/model/' + this.data.sourceItem.id, newHumanBeing)
          .subscribe();
      } else {
        this.http
          .post('http://localhost:8080/api/v1/model', newHumanBeing)
          .subscribe();
      }
      this.dialogRef.close();
    } else {
      console.log('New model form invalid');
      console.log(this.getFormValidationErrors());
    }
  }

  onCancel() {
    this.dialogRef.close();
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
