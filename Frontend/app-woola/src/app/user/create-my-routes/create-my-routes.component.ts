import { Component, OnInit } from '@angular/core';
import { Route } from '../../models/route.model';
import { FormBuilder, FormGroup, FormsModule, Validators, FormArray, ReactiveFormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';
import {Location} from '../../models/location.model';
import { RouteService } from '../../services/route.service';
import { LocationService } from '../../services/location.service';

@Component({
  selector: 'app-create-my-routes',
  standalone: true,
  imports: [CommonModule,FormsModule,ReactiveFormsModule],
  templateUrl: './create-my-routes.component.html',
  styleUrl: './create-my-routes.component.css'
})

export class CreateMyRoutesComponent implements OnInit{
  routeForm!: FormGroup;
  locationForm!: FormGroup;

  location:Location ={} as Location;

  file ={} as File;
  route ={} as Route;

  locations: Location[] = [];
  showLocations:boolean =false;

  createEventId!: number;

  constructor(private fb: FormBuilder, private routeService: RouteService,private locationService: LocationService,) { }

  ngOnInit(): void {
    this.routeForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      lat: ['', Validators.required],
      lng: ['', Validators.required],
    });

    this.locationForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      lat: ['', Validators.required],
      lng: ['', Validators.required],

    });
  }

  createRoute() {
    const formData = new FormData();
    if(this.file){
      formData.append('newImage', this.file);
    }
    formData.append('name', this.route.name);
    formData.append('description', this.route.description);
    formData.append('lat', this.route.lat.toString());
    formData.append('lng', this.route.lng.toString());

    this.routeService.create(formData).subscribe((response: any) =>{
      this.createEventId = response.id;
      this.showLocations = true;
      this.routeForm.reset();
    })
  }

  onFileSelected(e: any) {
    this.file = e.target.files[0];
    let fileInput = "";
    if (this.file)
      fileInput = this.file.name;
  }

  addLocation() {
    if (this.locationForm.valid) {
      this.locations.push(this.locationForm.value);

      this.location.routeId = this.createEventId;

      this.locationService.addLocation(this.location).subscribe(response=>{
      this.locationForm.reset();

    })

    }
  }

  resetForm() {
    if (confirm('¿Estás seguro de que deseas finalizar la creación de esta ruta?')) {
      this.routeForm.reset();
      this.locationForm.reset();
      this.locations = [];
      this.showLocations = false;
    }
  }
}
