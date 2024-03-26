package com.woola.woola.model.restModel;

import com.woola.woola.model.Location;

public class LocationDTO {
    public Long id;
    public String name;
    public String lon;
    public String lat;

    public LocationDTO(Location location){
        this.id = location.getId();
        this.name = location.getName();
        this.lon = location.getLon();
        this.lat = location.getLat();
    }
}
