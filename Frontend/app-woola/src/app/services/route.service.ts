import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Route } from '../models/route.model';


const BASE_URL = '/api/routes';
@Injectable({
  providedIn: 'root'
})
export class RouteService {

  constructor(private http: HttpClient) { }

  allRoutes():Observable<any>{
    return this.http.get(BASE_URL+"/all").pipe() as Observable<Route[]>;
  }
  routeById(id:Number):Observable<Route>{
    return this.http.get(BASE_URL+"/"+id).pipe() as Observable<Route>;
  }
  giveLike(id:Number){
    const body={};
    return this.http.post(BASE_URL+'/like/'+id,body).pipe();
  }
  giveDislike(id:Number){
    const body={};
    return this.http.post(BASE_URL+'/dislike/'+id,body).pipe();
  }
  create(formData: FormData) {
    return this.http.post(BASE_URL + "/new", formData).pipe();
  }

  delete(id: Number) {
    return this.http.delete(BASE_URL + "/" + id).pipe();
  }

  edit(formData: FormData, id: Number) {
    return this.http.put(BASE_URL + "/" + id, formData).pipe();
  }

  sendImage(formData: FormData, id: Number) {
    console.log((formData.get("newImage")as File).size)
    return this.http.put(BASE_URL + "/image/" + id, formData).pipe();
  }
}
