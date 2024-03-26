import { Injectable } from '@angular/core';
import {Router} from '@angular/router';

import { UserService } from './user.service';
import { User } from '../models/user.model';

import { HttpClient } from '@angular/common/http';

const BASE_URL = '/api/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  logged!: boolean;
  user: User | undefined;

  constructor(private http: HttpClient, private router: Router, private userService: UserService) {
    this.reqIsLogged();
  }

  getUser(){
    return this.user;
  }

  reqIsLogged(){
    this.userService.getMe().subscribe(
      response=>{
        this.user = response;
        this.logged = true;
      },
      error => console.log(error)
    );
  }

  logIn(username:string, password:string){
    this.http.post(BASE_URL+'/login', { username: username, password: password }, { withCredentials: true }).subscribe(
      (response)=> this.reqIsLogged(),
      (error) => alert("Credenciales invalidas")
    );
    this.router.navigate(['/']);
  }

  logOut(){
    return this.http.post(BASE_URL + '/logout', { withCredentials: true }).subscribe((resp: any)=>{
      this.logged = false;
      this.user = undefined;
      this.router.navigate(['/']);
    })
  }

  isLogged() {
    return this.logged;
  }

  isAdmin() {
    return this.user && (this.user.rol == "ADMIN");
  }

  isBase() {
    return this.user && (this.user.rol == "BASE");
  }

  isAso() {
    return this.user && (this.user.rol == "ASO");
  }
}
