package com.woola.woola.model.restModel;

import com.woola.woola.model.Comment;

public class CommentDTO {
    public Long id;
    public RouteDTO route;
    public String comment_user;
    public String description;
    public String time;
    public int totalLikes;

    public CommentDTO(Comment comment){
        this.id = comment.getId();
        this.route = new RouteDTO(comment.getRoute());
        this.comment_user = comment.getCommentUser();
        this.description = comment.getDescription();
        this.time = comment.getTime();
        this.totalLikes=comment.getTotalFavorites();
    }
}
