package com.woola.woola.model.restModel;

import com.woola.woola.model.Location;

public class LocationDTO {
    public Long id;
    public RouteDTO route;
    public String name;
    public Float lng;
    public Float lat;

    public LocationDTO(Location location){
        this.id = location.getId();
        this.route = new RouteDTO(location.getRoute());
        this.name = location.getName();
        this.lng = location.getLng();
        this.lat = location.getLat();
    }
}
