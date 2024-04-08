import { Component } from '@angular/core';
import { Route } from '../../../models/route.model';
import { ActivatedRoute, Router } from '@angular/router';
import { RouteService } from '../../../services/route.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-route',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './edit-route.component.html',
  styleUrl: './edit-route.component.css'
})
export class EditRouteComponent {
  route!: Route;
  file = {} as File;
  areImage:boolean=false;

  constructor(private router: Router, activatedRoute: ActivatedRoute, private routeService: RouteService) {
    const idEvent = activatedRoute.snapshot.params['id'] as number;
    this.load(idEvent);
  }

  load(id: number){
    this.routeService.routeById(id).subscribe((response)=>{
      this.route = response;
    });
  }

  ngOnInit() {

  }

  selected(i:boolean){
    if(i){
      return true;
    }else{
      return false;
    }
  }

  editRoute(e : any) {

    const formData = new FormData();
    formData.append('name', this.route.name);
    formData.append('description', this.route.description);
    formData.append('location', this.route.duration);
    formData.append('booking', this.route?.booking?.toString() ?? 'false');
    this.routeService.edit(formData, this.route.id).subscribe(response=>{

      if (this.areImage){
        const formDataImg=new FormData();
        formDataImg.append('newImage', this.file);
        this.routeService.sendImage(formDataImg,this.route.id).subscribe(response=>{});

      }
      this.router.navigate(['admin/routes']);
    });
  }
  onFileSelected(e: any) {
    this.file = e.target.files[0];
    this.areImage=true;
    let fileInput = "";
    if (this.file)
      fileInput = this.file.name;
  }
}
