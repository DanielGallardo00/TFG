package com.woola.woola.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="Id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
    private String comment_user;
    private String description;
    private String time;
    @ManyToMany(cascade=CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<User> favorites = new HashSet<>();
    
    public Comment (){}
    
    public Comment(String comment_user, String description, String time){
    this.comment_user = comment_user;
    this.description = description;
    this.time = time;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Comment other = (Comment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    public boolean isUserInFavorites(User user){
        return favorites.contains(user);
    }

    public int getTotalFavorites(){
        return favorites.size();
    }

    public boolean addFavorites(User user){
        return favorites.add(user);
    }

    public boolean removeFavorites(User user){
        return favorites.remove(user);
    }

    public String getCommentUser() {
        return comment_user;
    }
    public void setCommentUser(String comment_user) {
        this.comment_user = comment_user;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent(){
        return this.event;
    }

    public void clear(){
        this.favorites.clear();
    }

    public Long getId(){
        return this.id;
    }
}
