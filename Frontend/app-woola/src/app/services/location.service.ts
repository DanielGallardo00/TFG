import { Injectable } from '@angular/core';

import{Location} from '../models/location.model'
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_URL = 'api/location';

@Injectable({
  providedIn: 'root'
})
export class LocationService {

  constructor(private httpClient: HttpClient) { }

  addLocation(location: Location){
    const body={description: location.description}
    return this.httpClient.post(BASE_URL+'/new/'+location.routeId,location).pipe();
  }

  locationList(idRoute:number): Observable<Location[]>{
    return this.httpClient.get(BASE_URL+'s/'+idRoute).pipe() as Observable<Location[]>;
  }
}
