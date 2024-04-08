import { CommonModule } from '@angular/common';
import { Component,OnInit,QueryList,ViewChild, ViewChildren } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GoogleMap,MapInfoWindow, MapMarker, MapPolyline, MapDirectionsRenderer, MapDirectionsService} from '@angular/google-maps';
import { Observable, map } from 'rxjs';
import { CommentsComponent } from './comments/comments.component';
//import { Route } from '../models/route.model';

interface Route {
  id: number;
  name: string;
  description: string;
  locations: Location[];
}

interface Location {
  id: number;
  name:string,
  description: string;
  routeId: number;
  lat: number;
  lng: number;
}

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [GoogleMap, MapInfoWindow, MapMarker, MapPolyline, MapDirectionsRenderer, CommonModule,FormsModule,CommentsComponent],
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})

export class MapComponent implements OnInit{

locations: Location[] = [];

routes: Route[] = [
    {
      id: 1,
      name: "Ruta 1",
      description: "Esta es la ruta 1",
      locations: [
        {
          id: 1,
          routeId: 1,
          name:"Localización 1-1",
          description:"descripción 1",
          lat: 40.415278,
          lng: -3.692528
        },
        {
          id: 2,
          routeId: 1,
          name:"Localización 1-2",
          description:"descripción 2",
          lat: 40.410403,
          lng: -3.691783
        },
      ]
    },
    {
      id: 2,
      name: "Ruta 2",
      description: "Esta es la ruta 2",
      locations: [
        {
          id: 3,
          routeId: 2,
          name:"Localización 2-1",
          description:"descripción 1",
          lat: 40.405,
          lng: -3.685
        },
        {
          id: 4,
          routeId: 2,
          name:"Localización 2-2",
          description:"descripción 2",
          lat: 40.402,
          lng: -3.678
        },
        {
          id: 5,
          routeId: 2,
          name:"Localización 2-3",
          description:"descripción 3",
          lat: 40.398,
          lng: -3.672
        },
      ]
    },
    {
      id: 3,
      name: "Ruta 3",
      description: "Esta es la ruta 3",
      locations: [
        {
          id: 6,
          routeId: 3,
          name:"Localización 3-1",
          description:"descripción 1",
          lat: 40.42,
          lng: -3.70
        },
        {
          id: 7,
          routeId: 3,
          name:"Localización 3-2",
          description:"descripción 2",
          lat: 40.423,
          lng: -3.705
        },
        {
          id: 8,
          routeId: 3,
          name:"Localización 3-3",
          description:"descripción 3",
          lat: 40.426,
          lng: -3.71
        },
      ]
    },
    {
      id: 4,
      name: "Ruta por la Sierra de Madrid",
      description: "Disfruta de un recorrido por los bellos paisajes de la Sierra de Madrid.",
      locations: [
        {
          id: 9,
          routeId: 4,
          name: "Puerto de Navacerrada",
          description: "Disfruta de las vistas desde este emblemático puerto de montaña.",
          lat: 40.754722,
          lng: -3.971111,
        },
        {
          id: 10,
          routeId: 4,
          name: "Monasterio de El Escorial",
          description: "Visita este impresionante complejo monumental.",
          lat: 40.583333,
          lng: -3.916667,
        },
        {
          id: 11,
          routeId: 4,
          name: "Valle de los Caídos",
          description: "Conoce este lugar histórico y controvertido.",
          lat: 40.616667,
          lng: -3.883333,
        },
        {
          id: 12,
          routeId: 4,
          name: "Real Sitio de San Lorenzo de El Escorial",
          description: "Pasea por este conjunto histórico y artístico.",
          lat: 40.580556,
          lng: -3.902778,
        },
        {
          id: 13,
          routeId: 4,
          name: "La Granja de San Ildefonso",
          description: "Descubre este palacio real y sus impresionantes jardines.",
          lat: 40.577222,
          lng: -4.008333,
        },
        {
          id: 14,
          routeId: 4,
          name: "Parque Nacional de Guadarrama",
          description: "Explora la naturaleza en este parque nacional.",
          lat: 40.741667,
          lng: -3.775,
        },
      ],
    },
  ];
markerPositions: google.maps.LatLngLiteral[] =[];
selectedRoute: Route | null = null;
markers: MapMarker[] = [];

// polylinePath: google.maps.Polyline | undefined;


private routePolylines: Map<number, google.maps.Polyline> = new Map();

// points: google.maps.LatLngLiteral[] =[
//   {lat: 40.42, lng:-3.70},
//   {lat: 40.423, lng:-3.705},
//   {lat: 40.426, lng:-3.71},
// ];

// readonly directionsResults$: Observable<google.maps.DirectionsResult|undefined>

constructor(mapDirectionsService: MapDirectionsService){

  // const request: google.maps.DirectionsRequest={
  //   destination:{lat: 40.426, lng:-3.71},
  //   origin:{lat: 40.42, lng:-3.70},
  //   travelMode: google.maps.TravelMode.WALKING,
  // };
  // this.directionsResults$ = mapDirectionsService.route(request).pipe(map(response => response.result));
}

ngOnInit(): void {

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

      this.routeInfo = selectedRoute;

      this.locations = selectedRoute.locations;
    }
  }

}

getSelectedRoute(lat:number, lng:number): Route | undefined{
  for (const route of this.routes) {
    for (const location of route.locations) {
      if (location.lat === lat && location.lng === lng) {
        return route;
      }
    }
  }
  return undefined;
}

}
