package com.woola.woola.controller.restController;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woola.woola.model.Comment;
import com.woola.woola.model.Location;
import com.woola.woola.model.Route;
import com.woola.woola.model.User;
import com.woola.woola.model.restModel.CommentDTO;
import com.woola.woola.model.restModel.LocationDTO;
import com.woola.woola.service.LocationService;
import com.woola.woola.service.RouteService;
import com.woola.woola.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
//@RequestMapping("/api/locations")
public class LocationRestController {
    @Autowired
    private LocationService locationService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private UserService userService;


    @Operation(summary = "Get location")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "location obtained sucessfully",content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LocationDTO.class))}),
        @ApiResponse(responseCode = "400", description = "invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "401", description = "location is not created", content = @Content),
        @ApiResponse(responseCode = "403", description = "not enough privileges or admin is modifying itself", content = @Content),
        @ApiResponse(responseCode = "404", description = "location not found", content = @Content)
            
    })

    @GetMapping("/api/location/{id}")
    public ResponseEntity<LocationDTO> getLocation(@PathVariable long id) {
        Optional<Location> location = locationService.findById(id);
        if (location.isPresent()) {
            return new ResponseEntity<>(new LocationDTO(location.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Post location")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "location posted sucessfully",content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Location.class))}),
        @ApiResponse(responseCode = "400", description = "invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "401", description = "location is not created", content = @Content),
        @ApiResponse(responseCode = "403", description = "not enough privileges or admin is modifying itself", content = @Content),
        @ApiResponse(responseCode = "404", description = "location not found", content = @Content)
            
    })

    @PostMapping("/api/location/new/{id}")//tested
	public ResponseEntity<LocationDTO> postlocation(@PathVariable long id, @RequestBody Location newLocation, HttpServletRequest request) throws URISyntaxException{
        System.out.println("crear");
        Principal principal = request.getUserPrincipal();
        if(principal != null){
            try {
                locationService.save(newLocation);
            
                Route route = routeService.findById(id).orElseThrow();
                route.addLocation(newLocation);
                routeService.save(route);
                URI location = new URI("https://127.0.0.1:8443/api/locations/"+newLocation.getId());
                return ResponseEntity.created(location).body(new LocationDTO(newLocation));
            } catch (NoSuchElementException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } 
	}

    @Operation(summary = "Remove location")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "location removed sucessfully",content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LocationDTO.class))}),
        @ApiResponse(responseCode = "400", description = "invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "401", description = "location is not created", content = @Content),
        @ApiResponse(responseCode = "403", description = "not enough privileges or admin is modifying itself", content = @Content),
        @ApiResponse(responseCode = "404", description = "location not found", content = @Content)
            
    })

    @DeleteMapping("/api/location/{id}")//tested
	public ResponseEntity<LocationDTO> deleteLocation(@PathVariable long id, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        System.out.println(user.getRol());
        if(!user.getRol().equals("ADMIN")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(locationService.findById(id).isPresent()){
            locationService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}
    @Operation(summary = "Get list of locations of route")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "list of locations sucessfully getted",content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "array", implementation = LocationDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "route not found", content = @Content)      
    })
    @GetMapping("/api/locations/{id}")
    public ResponseEntity<List<LocationDTO>> deleteRouteLocations(@PathVariable long id){
        Optional<Route> routeOp=routeService.findById(id);
        if(routeOp.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Route route = routeOp.get();
        List<LocationDTO> list = new ArrayList<LocationDTO>();
        for(Location location:route.getLocations()){
            list.add(new LocationDTO(location));
        }
        return new ResponseEntity<>(list,HttpStatus.OK);
        
    }
}
