import { Injectable } from '@angular/core';

import { User } from '../models/user.model';

import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs';

const BASE_URL = '/api/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient,private router: Router) { }

  getMe(): Observable<User>{
    return this.http.get(BASE_URL+'/me', { withCredentials: true }).pipe()as Observable<User>;
  }

  getUser(id:Number): Observable<User>{
    return this.http.get(BASE_URL+'/admin/'+id, { withCredentials: true }).pipe()as Observable<User>;
  }

  editUser(name:string,email:string): Observable<User>{
    return this.http.patch(BASE_URL + "/me?newName="+name+"&newEmail="+email,{ withCredentials: true }).pipe()as Observable<User>;
  }

  getFavs(){
    return this.http.get(BASE_URL +"/me/favorites").pipe()as Observable<Event[]>;
  }

  removeFav(id:Number){
    return this.http.delete(BASE_URL +"/me/favorites/"+id);
  }

  register(username:string,password:string,rol:string,email:string){
    return this.http.post(BASE_URL + "/", {"username":username,"encodedPassword":password,"rol":rol,"email":email}).pipe(
      map((response: any) => {
        this.router.navigate(['/']);
        return response;
      }),
      catchError((error: any) => {
        return throwError('Algo salio mal');
      })
    );
  }

  addFavorites(id:number){
    const body={};
    return this.http.post(BASE_URL+'/me/favorites/'+id,body).pipe();
  }

  allUsers():Observable<any>{
    return this.http.get(BASE_URL+"/all").pipe() as Observable<User[]>;
  }

  deleteUser(id: number) {
    return this.http.delete(BASE_URL+"/admin/"+id);
  }

  adminEditUser(id: number, user: User) {
    const body = {};
    return this.http.patch(BASE_URL + '/admin/'+id+"?newName="+user.username+"&newEmail="+user.email+"&newRol="+user.rol,body).pipe();
  }
}
