import { Component } from '@angular/core';
import { UserService } from '../services/user.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  username!: string;
  password!: string;
  rol:string= "BASE";
  email!:string;

  constructor(private userService:UserService){}

  register(){
    this.userService.register(this.username,this.password,this.rol,this.email).subscribe(
      response =>{
        console.log(response);
      }
    );
  }
}
