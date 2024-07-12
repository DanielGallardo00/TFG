package com.woola.woola.controller.restController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.woola.woola.model.Comment;
import com.woola.woola.model.Route;
import com.woola.woola.model.User;
import com.woola.woola.service.CommentService;
import com.woola.woola.service.RouteService;
import com.woola.woola.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/routes")
public class RouteRestController {
    @Autowired
    private RouteService routeService;

    @Autowired
    private UserService userService;

      @Operation(summary = "Get Route by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route getted succesfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Route.class)) }),
            @ApiResponse(responseCode = "404", description = "Route not found", content = @Content)

    })
    @GetMapping("/{id}")
    public ResponseEntity<Route> getRoute(@PathVariable long id) {
        Optional<Route> ev = routeService.findById(id);
        if (ev.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ev.get(), HttpStatus.OK);
    }

     @Operation(summary = "Delete route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "route removed sucessfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Route.class)) }),
            @ApiResponse(responseCode = "401", description = "no user register", content = @Content),
            @ApiResponse(responseCode = "403", description = "not enouth privileges", content = @Content),
            @ApiResponse(responseCode = "404", description = "route not found", content = @Content)

    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Route> deleteRoute(@PathVariable long id, HttpServletRequest request) {
        Optional<Route> routeOp = routeService.findById(id);
        if (routeOp.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Route route = routeOp.get();
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return new ResponseEntity<Route>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOp = userService.findByUsername(principal.getName());
        if (userOp.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userOp.get();
        if (user.getRol().equals("BASE")) {
            if (!route.getUser().equals(user)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            route= clearRoute(route);
            routeService.deleteById(route.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (user.getRol().equals("ADMIN")) {
            route = clearRoute(route);
            routeService.deleteById(route.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

     @Operation(summary = "Create Route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "route created sucessfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Route.class)) }),
            @ApiResponse(responseCode = "401", description = "no user register", content = @Content),
            @ApiResponse(responseCode = "403", description = "not enouth privileges", content = @Content),

    })
    @PostMapping("/")
    public ResponseEntity<Route> createRoute(MultipartFile newImage, Route route, HttpServletRequest request)
            throws SQLException, IOException, URISyntaxException {
        System.out.println(newImage == null);
        route.setImgUrl(getBlob(newImage));
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOp = userService.findByUsername(principal.getName());
        if (userOp.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userOp.get();
        if (user.getRol().equals("BASE")) {
            
            route.setUser(userOp.get());
            
            routeService.save(route);

            user.addRoute(route);
            userService.save(user);

            URI location = new URI("https://127.0.0.1:8443/api/routes/" + route.getId());
            return ResponseEntity.created(location).body(route);
        } else if (user.getRol().equals("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


        @Operation(summary = "Edit route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "route edited sucessfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Route.class)) }),
            @ApiResponse(responseCode = "401", description = "no user register", content = @Content),
            @ApiResponse(responseCode = "403", description = "not enouth privileges", content = @Content),
            @ApiResponse(responseCode = "404", description = "route not found", content = @Content)

    })
    @PutMapping("/{id}") // testeado
    public ResponseEntity<Route> editRoute(@PathVariable long id, Route route, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        Optional<Route> routeDBOp = routeService.findById(id);
        if (routeDBOp.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Route routeDB = routeDBOp.get();
        route.setUser(routeDB.getUser());
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOp = userService.findByUsername(principal.getName());
        if (userOp.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userOp.get();
        if (user.getRol().equals("BASE")) {

            if (!routeDB.getUser().equals(user)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            route.setId(id);
            routeService.save(route);
            return new ResponseEntity<>(route, HttpStatus.OK);
        } else if (user.getRol().equals("ADMIN")) {
            route.setId(id);
            routeService.save(route);
            return new ResponseEntity<>(route, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @Operation(summary = "Change route image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "route image changed sucessfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Route.class)) }),
            @ApiResponse(responseCode = "401", description = "no user register", content = @Content),
            @ApiResponse(responseCode = "403", description = "not enouth privileges", content = @Content),
            @ApiResponse(responseCode = "404", description = "route not found", content = @Content)

    })
    @PutMapping("/image/{id}") // testado
    public ResponseEntity<Route> setImage(@PathVariable long id, MultipartFile newImage, HttpServletRequest request)
            throws SQLException, IOException, URISyntaxException {
        Optional<Route> routeOp = routeService.findById(id);
        if (routeOp.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Route route = routeOp.get();
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOp = userService.findByUsername(principal.getName());
        if (userOp.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userOp.get();
        if (user.getRol().equals("BASE")) {
            if (!route.getUser().equals(user)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            System.out.println("------------------size: "+newImage.getSize());
            System.out.println("------------------size: "+getBlob(newImage).length());
            route.setImgUrl(getBlob(newImage));
            routeService.save(route);
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (user.getRol().equals("ADMIN")) {
            route.setImgUrl(getBlob(newImage));
            routeService.save(route);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @Operation(summary = "Get route image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "route image getted sucessfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Route.class)) }),
            @ApiResponse(responseCode = "404", description = "route not found", content = @Content)

    })
    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable long id) throws SQLException, IOException {
        Optional<Route> routeOp = routeService.findById(id);
        if (routeOp.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Route route = routeOp.get();
        Resource image = getFile(route.getImage());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(image.contentLength())
                .body(image);
    }

    @Operation(summary = "Get route list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of routes correct", content = @Content)

    })
    @GetMapping("/all")
    public ResponseEntity<List<Route>> getAllRoutes() {
        return new ResponseEntity<>(routeService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Give like to route")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "like done succesfully",content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Route.class))}),
        @ApiResponse(responseCode = "401", description = "no user register", content = @Content),
        @ApiResponse(responseCode = "404", description = "route not found", content = @Content)
            
    })
    @PostMapping("/like/{id}")
    public ResponseEntity<Route> giveLike(@PathVariable long id, HttpServletRequest request){
        Optional<Route> routeOp = routeService.findById(id);
        if(routeOp.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Route route = routeOp.get();

        Principal principal = request.getUserPrincipal();
        if(principal==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOp = userService.findByUsername(principal.getName());
        if(userOp.isEmpty()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userOp.get();
        route.addLike(user);
        routeService.save(route);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "Give dislike to route")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "dislike done succesfully",content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Route.class))}),
        @ApiResponse(responseCode = "401", description = "no user register", content = @Content),
        @ApiResponse(responseCode = "404", description = "route not found", content = @Content)
            
    })
    @PostMapping("/dislike/{id}")
    public ResponseEntity<Route> giveDislike(@PathVariable long id, HttpServletRequest request){
        Optional<Route> routeOp = routeService.findById(id);
        if(routeOp.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Route route = routeOp.get();

        Principal principal = request.getUserPrincipal();
        if(principal==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOp = userService.findByUsername(principal.getName());
        if(userOp.isEmpty()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userOp.get();
        route.addDislike(user);
        routeService.save(route);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // =====================auxiliar functions=======================
    public Blob getBlob(MultipartFile file) throws SQLException, IOException {
        Blob myBlob;
        byte[] bytes = file.getBytes();
        myBlob = new SerialBlob(bytes);
        return myBlob;
    }


    public Resource getFile(Blob image) throws SQLException, JsonProcessingException {

        byte[] data;
        Resource resource = null;
        data = image.getBytes(1, (int) image.length());
        resource = new ByteArrayResource(data);
        return resource;
    }

    private Route clearRoute(Route route) {
        List<User> users = userService.findAll();
        for (User user : users) {
            if (user.isInFavorites(route)) {
                user.removeFavoritos(route);
                userService.save(user);
                System.out.println("eliminado");
            }

        }
        // List<Comment> comments = route.getComments();
        // for (Comment comment : comments) {
        //     comment.clear();
        //     commentService.save(comment);
        //     commentService.deleteById(comment.getId());
        // }
        //route.clear();
        routeService.save(route);
        return route;
    }
}
