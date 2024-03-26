package com.woola.woola.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    private String email;
    private String username;
    //@JsonIgnore
    private String encodedPassword;
    private String rol;

    @ManyToMany
    private List<Route> favoritos = new ArrayList<>();

    private Boolean validated;
    private String checkToken;
    
    public User(){}

    //base user
    public User(String email, String username, String encodedPassword){
        this.username = username;
        this.email = email;
        this.encodedPassword = encodedPassword;
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
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public void setCheckToken(String token){
        this.checkToken=token;
    }

    public String getCheckToken(){
        return this.checkToken;
    }

    public void setValidated(Boolean validated){
        this.validated=validated;
    }

    public Boolean getValidated(){
        return this.validated;
    }

    public void setId(long id){
        this.id=id;
    }

    public Long getId(){
        return this.id;
    }

    public List<Route> getFavoritos(){
        return this.favoritos;
    }

    public boolean isInFavorites(Route route){
        return this.favoritos.contains(route);
    }

    public void setFavoritos(List<Route> favoritos){
        this.favoritos = favoritos;
    }

    public void addFavoritos(Route route){
        this.favoritos.add(route);
    }

    public void removeFavoritos(Route route){
        this.favoritos.remove(route);
    }

    public String getUsername(){
        return this.username;
    }

    public String getEmail(){
        return this.email;
    }

    public String getencodedPassword(){
        return this.encodedPassword;
    }

    public String getRol(){
        return this.rol;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setencodedPassword(String encodedPassword){
        this.encodedPassword = encodedPassword;
    }

    public void setRol(String rol){
        this.rol =  rol;
    }
}
