package com.woola.woola.model.restModel;

import com.woola.woola.model.Route;

public class RouteDTO {
    public Long id;
    private String name;
    private String description;
    private Long userId;

    public RouteDTO(Route route){
        this.id = route.getId();
        this.name = route.getName();
        this.description = route.getDescription();
        this.userId = route.getUserId();
    }
}
