import { Routes } from '@angular/router';
import { NewModelComponent } from './new-model/new-model.component';
import { MainComponent } from './main/main.component';

export const routes: Routes = [
    {
        path: '', component: MainComponent
    },
    { 
        path: 'newModel', component: NewModelComponent
    }
];
