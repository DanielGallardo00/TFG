package com.woola.woola.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woola.woola.model.Route;
import com.woola.woola.model.User;
import com.woola.woola.repository.RouteRepository;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    public void save(Route route){
        routeRepository.save(route);
    }

    public List<Route> findAll(){
        return routeRepository.findAll();
    }

    public Optional<Route> findById(Long id){
        Optional<Route> findById = routeRepository.findById(id);
        return findById;
    }

    public List<Route> findAllbyUser(User user) {
		return routeRepository.findAllByUser(user.getId());
	}
    
    public void updateById(Route route, long id){
        int booking=0;
        if(route.getBooking()){
            booking=1;
        }
        routeRepository.updateRoute(id,route.getName(),route.getBooking(),route.getDescription(),route.getImage());
    }

	public Optional<Route> findById(long id) {
		Optional<Route> findById = routeRepository.findById(id);
		return findById;
	}

    public void deleteById(long id){
		routeRepository.deleteById(id);
	}


}
