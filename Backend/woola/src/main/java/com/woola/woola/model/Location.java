package com.woola.woola.model;

import java.sql.Blob;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="Id")
    private Long id;

    private String name;

    @JsonIgnore
    @Lob
    private Blob image;

    private String lon;
    private String lat;

    @ManyToOne(fetch = FetchType.LAZY)
    private Route route;

    public Location(){}

    public Location(String name, String lat, String lon, Blob imgUrl){
        this.name = name;
        this.lat = lat;
        this.lon = lon;
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
        Location other = (Location) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Blob getImage() {
        return image;
    }
    
    public void setImgUrl(Blob image) {
        this.image = image;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Route getRoute(){
        return this.route;
    }

}
