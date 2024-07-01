import { Component, OnInit } from '@angular/core';

import {User} from '../models/user.model'
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBars, faUser, faStar } from '@fortawesome/free-solid-svg-icons';

import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [RouterOutlet,RouterLink, FontAwesomeModule, CommonModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent implements OnInit {

  u?:User;
  username?:string;

  faGauge=faStar;
  faBars = faBars;
  faUser = faUser;

  showme:boolean = true;

  constructor(private userService:UserService, private authService:AuthService ,private router: Router){}

  ngOnInit(): void {
    this.loadUser();
  }

  loadUser(){
    this.userService.getMe().subscribe(
      response=>{
        this.u = response;
        this.username=this.u.username;
        if(!(this.u.rol === "BASE"))
          this.router.navigate(['']);
      },
      error =>{
        this.router.navigate(['']);
      }
    )
  }

  logout(){
    this.authService.logOut();
  }

  toogleSidebar(){
    this.showme = !this.showme;
  }
}
