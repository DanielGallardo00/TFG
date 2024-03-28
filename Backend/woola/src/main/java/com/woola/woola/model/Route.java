package com.woola.woola.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import java.sql.Blob;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="Id")
    private Long id;

    private String name;
    private String description;
    private String duration;

    @JsonIgnore
    @ManyToOne()
    private User user;


    private boolean booking;

    @JsonIgnore
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    private Set<User> likeList = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    private Set<User> dislikeList = new HashSet<>();

    @JsonIgnore
    @Lob
    private Blob image;
    
    public Route (){}
    
    public Route(String name, String description, User user, String duration, Blob imgUrl, boolean booking){
        this.name = name;
        this.description = description;
        this.user = user;
        this.duration = duration;
        this.image = imgUrl;
        this.booking = booking;
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
        Route other = (Route) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public void setId(long id){
        this.id=id;
    }

    public boolean isUserInLikes(User user){
        return likeList.contains(user);
    }

    public int getTotalLikes(){
        return likeList.size();
    }

    public boolean addLike(User user){
        return likeList.add(user);
    }

    public boolean removeLike(User user){
        return likeList.remove(user);
    }

    public boolean isUserInDislikes(User user){
        return dislikeList.contains(user);
    }

    public int getTotalDislikes(){
        return dislikeList.size();
    }

    public boolean addDislike(User user){
        return dislikeList.add(user);
    }

    public boolean removeDislike(User user){
        return dislikeList.remove(user);
    }

    public Long getId(){
        return this.id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getBooking() {
        return booking;
    }
    public void setBooking(boolean booking) {
        this.booking = booking;
    }

    public User getUser() {
        return user;
    }

    public String getUserName() {
        return user.getUsername();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Blob getImage() {
        return image;
    }

    public void setImgUrl(Blob image) {
        this.image = image;
    }

    public List<Comment> getComments(){
        return this.comments;
    }

    public void setComments(List<Comment> comments){
        this.comments = comments;
    }

    public void addComment(Comment comment){
        comment.setRoute(this);
        this.comments.add(comment);
    }

    public void removeComment(Comment comment){
        comment.setRoute(null);
        this.comments.remove(comment);
    }
    
    public void clearComments(){
        for (Comment comment : comments) {
            removeComment(comment);
        }
    }



    public List<Location> getLocations(){
        return this.locations;
    }

    public void setLocations(List<Location> locations){
        this.locations = locations;
    }

    public void addLocation(Location location){
        location.setRoute(this);
        this.locations.add(location);
    }

    public void removeLocation(Location location){
        location.setRoute(null);
        this.locations.remove(location);
    }
    
    public void clearLocations(){
        for (Location location : locations) {
            removeLocation(location);
        }
    }


    public void clear() {
        likeList.clear();
        dislikeList.clear();
    }
    public List<Comment> deleteUserReferences(User user) {
        if(likeList.contains(user))
            removeLike(user);
        if(dislikeList.contains(user))
            removeDislike(user);
        List<Comment> output = new ArrayList<>();
        for(Comment comment:comments){
            if(comment.isUserInFavorites(user)){
                comment.removeFavorites(user);
                output.add(comment);
            } 
        }
        return output;
    }
}
