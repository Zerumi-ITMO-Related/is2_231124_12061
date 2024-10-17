import { Routes } from '@angular/router';
import { NewModelComponent } from './new-model/new-model.component';
import { MainComponent } from './main/main.component';
import { authenticationGuard } from './authentication.guard';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';

export const routes: Routes = [
  {
    path: '',
    canActivate: [authenticationGuard],
    children: [
      {
        path: 'login',
        component: LoginComponent,
      },
      {
        path: 'main',
        component: MainComponent,
      },
      {
        path: 'newModel',
        component: NewModelComponent,
      },
      {
        path: 'admin',
        component: AdminComponent,
      },
      {
        path: '**',
        redirectTo: '/login',
      },
    ],
  },
];
