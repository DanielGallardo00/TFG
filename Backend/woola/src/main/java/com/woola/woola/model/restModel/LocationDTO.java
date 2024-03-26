package com.woola.woola.model.restModel;

import com.woola.woola.model.Location;
import com.woola.woola.model.Route;

public class LocationDTO {
    public Long id;
    private String name;
    private String lon;
    private String lat;

    public RouteDTO(Location location){
        this.id = location.getId();
        this.name = location.getName();
        this.lon = location.lon();
        this.lat = location.lat();
    }
}
