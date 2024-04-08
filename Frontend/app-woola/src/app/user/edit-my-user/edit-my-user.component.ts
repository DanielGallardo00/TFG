import { Component, OnInit } from '@angular/core';
import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-my-user',
  standalone: true,
  imports: [RouterOutlet,RouterLink, FontAwesomeModule, CommonModule, FormsModule],
  templateUrl: './edit-my-user.component.html',
  styleUrl: './edit-my-user.component.css'
})

export class EditMyUserComponent implements OnInit {
  user?:User;
  username = this.user?.username;
  email = this.user?.email;

  constructor(private userService:UserService,private router: Router){

  }

  ngOnInit(): void {
      this.loadUser();
  }

  loadUser(){
    this.userService.getMe().subscribe(
      response=>{
        this.user = response;
        this.username=this.user.username;
        this.email=this.user.email;
      }
    );
  }

  onClickSubmit() {
    if(this.username != undefined && this.email != undefined){
      this.userService.editUser(this.username,this.email).subscribe(
        response =>{
          this.router.navigate(['/login'])
        }
      );

    }
  }

}
