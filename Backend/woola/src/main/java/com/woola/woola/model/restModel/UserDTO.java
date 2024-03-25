package com.woola.woola.model.restModel;

import com.woola.woola.model.User;

public class UserDTO {
    public Long id;
    public String email;
    public String username;
    public String rol;

    public UserDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.rol = user.getRol();
    }

}
