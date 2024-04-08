import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, CommonModule ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  constructor(public authService: AuthService, public router: Router){}

  enter(){
    if(this.authService.logged){
      this.router.navigate(['/map']);
    }else{
      this.router.navigate(['/login']);
    }
  }
}
