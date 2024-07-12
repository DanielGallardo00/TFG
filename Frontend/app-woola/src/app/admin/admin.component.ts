import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { User } from '../models/user.model';

import * as simpleDatatables from 'simple-datatables';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBars, faUser, faStar } from '@fortawesome/free-solid-svg-icons';

import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [RouterOutlet,RouterLink, FontAwesomeModule, CommonModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent implements OnInit{
  username?: string;
  u?:User;

  showme:boolean = true;

  constructor(private userService:UserService, private authService:AuthService, private router: Router) { }

  ngOnInit(): void {
    this.loadUser();
  }

  loadUser(){
    this.userService.getMe().subscribe(
      response=>{
        this.u = response;
        this.username=this.u.username;
        if(!(this.u.rol === "ADMIN"))
          this.router.navigate(['']);
      },
      error => {
        this.router.navigate(['']);
      }
    );
  }

  logout(){
    this.authService.logOut();
    this.router.navigate(['']);
  }

  toogleSidebar(){
    this.showme = !this.showme;
  }
}
