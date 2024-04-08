import { Routes } from '@angular/router';

import {UserComponent} from './user/user.component'
import { EditMyUserComponent } from './user/edit-my-user/edit-my-user.component';
import { MyUserFavsComponent } from './user/my-user-favs/my-user-favs.component';
import { AdminComponent } from './admin/admin.component';
import { LoginComponent } from './login/login.component';
import { UsersComponent } from './admin/users/users.component';
import { EditUserComponent } from './admin/users/edit-user/edit-user.component';
import { RoutesComponent } from './admin/routes/routes.component';
import { EditRouteComponent } from './admin/routes/edit-route/edit-route.component';
import { MapComponent } from './map/map.component';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './register/register.component';

export const routes: Routes = [
  {path: 'myUser', component: UserComponent,
  children:[
    {path:'', component:EditMyUserComponent},
    {path:'favorites', component:MyUserFavsComponent}
  ]
},
  {path: 'admin', component: AdminComponent,
  children: [
    {path: 'routes', component: RoutesComponent},
    {path: 'editroute/:id', component: EditRouteComponent},

    {path: 'users', component: UsersComponent},
    {path: 'edituser/:id', component: EditUserComponent},
   ]
   },
  {path:'login', component: LoginComponent},
  {path:'register', component: RegisterComponent},
  {path:'map', component: MapComponent},
  {path:'', component: HomeComponent}
];
