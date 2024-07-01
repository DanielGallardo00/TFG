import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

import { Comment } from '../models/comment.model';

const BASE_URL = 'api/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private httpClient: HttpClient, private router: Router) { }


  getComment(id: number){
    return this.httpClient.get(BASE_URL + '/' + id).pipe() as Observable<Comment>;
  }

  deleteComment(id: number){
      return this.httpClient.delete(BASE_URL + '/' + id, { withCredentials: true });
  }
  commentList(idRoute:number): Observable<Comment[]>{
    return this.httpClient.get(BASE_URL+'s/'+idRoute).pipe() as Observable<Comment[]>;
  }
  addComment(comment: Comment){
    const body={description: comment.description}
    return this.httpClient.post(BASE_URL+'/new/'+comment.route.id,body).pipe();
  }
  giveLike(id: number){
    const body={};
    return this.httpClient.post(BASE_URL+'/like/'+id,body).pipe();

  }
}
