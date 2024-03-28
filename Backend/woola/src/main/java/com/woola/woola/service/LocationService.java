package com.woola.woola.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woola.woola.model.Location;
import com.woola.woola.repository.LocationRepository;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public void save(Location location){
        locationRepository.save(location);
    }

    public List<Location> findAll(){
        return locationRepository.findAll();
    }

    public Optional<Location> findById(Long id){
        Optional<Location> findById = locationRepository.findById(id);
        return findById;
    }

    public void deleteById(long id){
		locationRepository.deleteById(id);
	}
}
