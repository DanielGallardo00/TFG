import { CommonModule } from '@angular/common';
import { Component,OnInit,QueryList,ViewChild, ViewChildren } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GoogleMap,MapInfoWindow, MapMarker, MapPolyline, MapDirectionsRenderer, MapDirectionsService} from '@angular/google-maps';
import { CommentsComponent } from './comments/comments.component';
import { RouteService } from '../services/route.service';
import { LocationService } from '../services/location.service';
import { Route } from '../models/route.model';
import { Location } from '../models/location.model';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';


@Component({
  selector: 'app-map',
  standalone: true,
  imports: [GoogleMap, MapInfoWindow, MapMarker, MapPolyline, MapDirectionsRenderer, CommonModule,FormsModule,CommentsComponent,RouterOutlet,RouterLink,],
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})

export class MapComponent implements OnInit{

locations: Location[] = [];

routeList!: Route[];

selected:Boolean = false;


markerPositions: google.maps.LatLngLiteral[] =[];
selectedRoute: Route | null = null;
markers: MapMarker[] = [];

constructor(mapDirectionsService: MapDirectionsService, private routeService:RouteService, private locationService: LocationService, private userService:UserService, private authService:AuthService,public router: Router){
}

ngOnInit(): void {
  this.routeService.allRoutes().subscribe(response=>{
    this.routeList = response;
  })
}

center: google.maps.LatLngLiteral ={
      lat: 40.4165,
      lng: -3.70256
};
zoom = 14;

@ViewChildren(MapInfoWindow) infoWindowsView!: QueryList<MapInfoWindow>;

selectedInfoWindowIndex: number | null = null;

openInfoWindow(marker: MapMarker, windowIndex: number) {
  if (this.selectedInfoWindowIndex !== null && this.selectedInfoWindowIndex !== windowIndex) {
    // Close the previously opened info window
    this.infoWindowsView.get(this.selectedInfoWindowIndex)?.close();
  }

  // Open the current info window
  this.infoWindowsView.get(windowIndex)?.open(marker);
  this.selectedInfoWindowIndex = windowIndex;
}


routeInfo: any = {};

show($event: google.maps.MapMouseEvent) {
  this.locations = [];
  const clickedPosition = $event.latLng;
  if (clickedPosition) {
    const lat = clickedPosition.lat();
    const lng = clickedPosition.lng();


    const selectedRoute = this.getSelectedRoute(lat, lng)

    if(selectedRoute !== undefined){
      this.selected = true;

      this.routeInfo = selectedRoute;

      this.loadLocations(selectedRoute.id);
    }
  }

}

loadLocations(routeId: number){
  this.locationService.locationList(routeId).subscribe(
    response => this.locations = response
  )
}

 getSelectedRoute(lat:number, lng:number): Route | undefined{
  for (const route of this.routeList) {
      if (route.lat === lat && route.lng === lng) {
        return route;
      }
  }
  return undefined;
 }

 imageURL(id:Number){
  return '/api/routes/image/'+id;
 }

 addFavorites(route:Route){
  if(this.authService.logged){
    console.log("Hola");
    this.userService.addFavorites(route.id).subscribe();
  }else{
    this.router.navigate(['/login'])
  }
 }
}
