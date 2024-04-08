import { Component, Input } from '@angular/core';
import { Route } from '../../models/route.model';
import { Comment } from '../../models/comment.model';
import { CommentService } from '../../services/comment.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-comments',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './comments.component.html',
  styleUrl: './comments.component.css'
})
export class CommentsComponent {

  @Input()
  routeC!:Route;

  commentList!:Comment[];
  comment: Comment ={} as Comment;
  description!: string;

  constructor(private commentService: CommentService, private authService:AuthService, private router: Router){}

  ngOnInit(){
    this.loadComments();
  }

  loadComments(){
    this.commentService.commentList(this.routeC.id).subscribe(
      comments=> this.commentList= comments
    )
  }

  save(){
    if(this.authService.logged){
      this.comment.route = this.routeC;
      this.comment.description= this.description;
      this.commentService.addComment(this.comment).subscribe(
        response =>{
          this.loadComments();
        });
      this.description="";
    }else{
      this.router.navigate(['/login']);
    }
  }
}
