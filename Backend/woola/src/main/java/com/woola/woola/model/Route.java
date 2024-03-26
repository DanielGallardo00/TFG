package com.woola.woola.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import java.sql.Blob;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="Id")
    private Long id;

    private String name;
    private String description;

    private String userId;

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
    
    public Route(String name, String description, String userId, Blob imgUrl){
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.image = imgUrl;
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
}
