import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { User } from '../../../models/user.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-user',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './edit-user.component.html',
  styleUrl: './edit-user.component.css'
})
export class EditUserComponent {
  user!: User;

  constructor(private router: Router, activatedRoute: ActivatedRoute,private userService: UserService){
    const idEvent = activatedRoute.snapshot.params['id'] as number;
    this.load(idEvent);
  }

  load(id: number){
    this.userService.getUser(id).subscribe((response)=>{
      this.user = response;
    });
  }
  ngOnInit() {

  }

  editUser(id: number) {
    this.userService.adminEditUser(id, this.user).subscribe(response=>{
      this.router.navigate(['admin/users']);
    });
  }
}
