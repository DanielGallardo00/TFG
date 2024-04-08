import { Component } from '@angular/core';
import { User } from '../../models/user.model';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent {
  id?: number;
  username?: string;
  email?: string;
  rol?: string;
  users?: User[];

  constructor(private router: Router, private userService: UserService) {}

  ngOnInit(){
    this.userService.allUsers().subscribe((response)=>{
      this.users=response;
    })
  }

  modify(id: number) {
     this.router.navigate(['/admin/edituser/', id]);
  }

  deleteUser(id: number) {
    this.userService.deleteUser(id).subscribe(
      response =>{
        this.ngOnInit();
      }
    )
  }
}
