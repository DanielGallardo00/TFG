package com.woola.woola.model.restModel;

import com.woola.woola.model.Route;

public class RouteDTO {
    public Long id;
    public String name;
    public String description;
    public String duration;
    public String user;
    public boolean booking;

    public RouteDTO(Route route){
        this.id = route.getId();
        this.name = route.getName();
        this.description = route.getDescription();
        this.duration = route.getDuration();
        this.user = route.getUserName();
        this.booking = route.getBooking();
    }
}
