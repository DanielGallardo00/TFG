import { Component } from '@angular/core';
import { Route } from '../../models/route.model';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-my-user-favs',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-user-favs.component.html',
  styleUrl: './my-user-favs.component.css'
})
export class MyUserFavsComponent {

  routes?: Route[]; // No entiendo el error

  constructor(private userService:UserService){}

  removeFav(id:Number){
    this.userService.removeFav(id).subscribe(
      response =>{
        console.log(response);
        this.getFavs();
      }
    )
  }

  ngOnInit(): void {
    this.getFavs();
  }

  imageURL(id:Number){
    return '/api/routes/image/'+id;
}

  getFavs(){
    this.userService.getFavs().subscribe(
      response=>{
        this.routes = response;
        for (let i = 0; i < response.length; i++) {
          console.log(this.routes[i]);
        }
      }
    );
  }
}
