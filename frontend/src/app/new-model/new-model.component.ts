import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HumanBeing } from '../model';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-new-model',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './new-model.component.html',
  styleUrl: './new-model.component.css'
})
export class NewModelComponent implements OnInit {
  newModelForm!: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient) {}

  ngOnInit(): void {
     this.newModelForm = this.fb.group({
        name: ['', Validators.required],
        x: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
        y: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
        realHero: [false],
        hasToothpick: [false],
        carName: ['', Validators.required],
        mood: ['', Validators.required],
        impactSpeed: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
        minutesOfWaiting: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
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
        this.http.post('http://localhost:8080/api/v1/model', newHumanBeing).subscribe();
     }
     else {
        console.log('New model form invalid');
        console.log(this.getFormValidationErrors())
     }
  }

  getFormValidationErrors() {
    Object.keys(this.newModelForm.controls).forEach(key => {
      const controlErrors: ValidationErrors = this.newModelForm.get(key)?.errors ?? [];
      if (controlErrors != null) {
        Object.keys(controlErrors).forEach(keyError => {
         console.log('Key control: ' + key + ', keyError: ' + keyError + ', err value: ', controlErrors[keyError]);
        });
      }
    });
  }
}
