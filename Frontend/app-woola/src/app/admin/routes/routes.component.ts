import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Route } from '../../models/route.model';
import { RouteService } from '../../services/route.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-routes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './routes.component.html',
  styleUrl: './routes.component.css'
})
export class RoutesComponent {
  id?: number;
  name?: string;
  duration?: string;
  userName?: string;
  routes?:Route[];

  constructor(private router: Router, private routeService:RouteService) {

  }
  ngOnInit(){
    this.routeService.allRoutes().subscribe((response)=>{
      this.routes=response;
    })
  }

  modify(id: number) {
     this.router.navigate(['/admin/editroute/', id]);
  }

  deleteRoute(id: number) {
    if (confirm('¿Estás seguro de que deseas eliminar esta ruta?')) {

      this.routeService.delete(id).subscribe(
        response =>{
          this.ngOnInit();
        });

    }

  }
}
