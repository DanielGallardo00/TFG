package com.woola.woola.controller.restController;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.woola.woola.model.Comment;
import com.woola.woola.model.Route;
import com.woola.woola.model.User;
import com.woola.woola.model.restModel.ChangePassword;
import com.woola.woola.model.restModel.RouteDTO;
import com.woola.woola.model.restModel.UserDTO;
import com.woola.woola.service.CommentService;
import com.woola.woola.service.RouteService;
import com.woola.woola.service.UserService;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    @Autowired
    private UserService userService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

     @Operation(summary = "Get a user by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "user is not registered", content = @Content),
            @ApiResponse(responseCode = "404", description = "user not found", content = @Content)

    })
    @GetMapping("/admin/{id}")
    public ResponseEntity<UserDTO> getProfile(@PathVariable long id, HttpServletRequest request) {
        if (userService.findById(id).isPresent()) {
            User user = userService.findById(id).get();
            try {
                User userPrincipal = userService.findByUsername(request.getUserPrincipal().getName()).orElseThrow();
                return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
            } catch (NoSuchElementException e) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

     @Operation(summary = "Get current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "user is not registered", content = @Content)

    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User user = userService.findByUsername(principal.getName()).get();
            return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Operation(summary = "Modify current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user modified sucessfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "user is not registered", content = @Content)

    })
    @PatchMapping("/me")
    public ResponseEntity<UserDTO> modifyUser(@RequestParam String newName, @RequestParam String newEmail,
            HttpServletRequest request) throws IOException, SQLException {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User userPrincipal = userService.findByUsername(request.getUserPrincipal().getName()).orElseThrow();
            userPrincipal.setUsername(newName);
            userPrincipal.setEmail(newEmail);
            userService.save(userPrincipal);
            return new ResponseEntity<>(new UserDTO(userPrincipal), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @Operation(summary = "Modify user by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user modified sucessfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "user is not registered", content = @Content),
            @ApiResponse(responseCode = "403", description = "not enough privileges or admin is modifying itself", content = @Content),
            @ApiResponse(responseCode = "404", description = "user not found", content = @Content)

    })
    @PatchMapping("/admin/{id}")
    public ResponseEntity<UserDTO> modifyUserbyAdmin(@PathVariable long id, @RequestParam String newName,
            @RequestParam String newEmail, @RequestParam String newRol, HttpServletRequest request)
            throws IOException, SQLException {
        if (userService.findById(id).isPresent()) {
            User user = userService.findById(id).get();
            try {
                User userPrincipal = userService.findByUsername(request.getUserPrincipal().getName()).orElseThrow();
                if (userPrincipal.getId() != user.getId()) {
                    user.setUsername(newName);
                    user.setEmail(newEmail);
                    user.setRol(newRol);
                    userService.save(user);
                    return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
                } else
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } catch (NoSuchElementException e) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    
    @Operation(summary = "Register a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "user created sucessfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "403", description = "existing user or wrong role", content = @Content)

    })
    @PostMapping("/")
    public ResponseEntity<UserDTO> register(@RequestBody User user) {
        if (!userService.existUsername(user.getUsername()) && !userService.existEmail(user.getEmail())) {
            if (!user.getRol().equals("BASE")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            user.setValidated(true);
            user.setencodedPassword(passwordEncoder.encode(user.getencodedPassword()));
            userService.save(user);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/admin/{id}")
                    .buildAndExpand(user.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    @Operation(summary = "Modify my password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "password modified sucessfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "user is not registered", content = @Content),
            @ApiResponse(responseCode = "403", description = "wrong password", content = @Content)

    })
    @PatchMapping("/me/password")
    public ResponseEntity<UserDTO> modifyMyPassWord(@RequestBody ChangePassword password, HttpServletRequest request)
            throws IOException, SQLException {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User userPrincipal = userService.findByUsername(request.getUserPrincipal().getName()).orElseThrow();
            if (passwordEncoder.matches(password.oldPassword, userPrincipal.getencodedPassword())) {
                userPrincipal.setencodedPassword(passwordEncoder.encode(password.newPassword));
                userService.save(userPrincipal);
                return new ResponseEntity<>(new UserDTO(userPrincipal), HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Operation(summary = "Delete user by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user deleted sucessfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "user is not registered", content = @Content),
            @ApiResponse(responseCode = "403", description = "not enough privileges or admin is deleting itself", content = @Content),
            @ApiResponse(responseCode = "404", description = "user not found", content = @Content)

    })
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User userPrincipal = userService.findByUsername(request.getUserPrincipal().getName()).orElseThrow();
            try {
                User user = userService.findById(id).orElseThrow();
                if (user.getId() != userPrincipal.getId()) {
                    List<Route> routes = routeService.findAll();
                    for (Route route : routes) {
                        List<Comment> routeComments = route.deleteUserReferences(user);
                        for (Comment comment : routeComments) {
                            commentService.save(comment);
                        }
                    }
                    if (user.getRol().equals("ASO")) {
                        List<Route> userRoutes = routeService.findAllbyUser(user);
                        for (Route userRoute : userRoutes) {
                            System.out.print("\n borrando evento \n");
                            userRoute = clearRoute(userRoute);
                            routeService.deleteById(userRoute.getId());
                        }
                        System.out.print("\n borrando asociacion \n");
                        userService.deleteById(id);
                    } else {
                        System.out.println("borrar caso normal");
                        userService.deleteById(id);
                    }

                    return new ResponseEntity<>(HttpStatus.OK);
                } else
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } catch (NoSuchElementException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Operation(summary = "Get user favorites by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "favourites from user getted sucessfully", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "array", implementation = RouteDTO.class))) }),
            @ApiResponse(responseCode = "400", description = "invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "user is not registered", content = @Content),
            @ApiResponse(responseCode = "403", description = "not enough privileges", content = @Content),
            @ApiResponse(responseCode = "404", description = "user not found", content = @Content)

    })
    @GetMapping("/admin/{id}/favorites")
    public ResponseEntity<List<RouteDTO>> getUserFavorites(@PathVariable long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User userPrincipal = userService.findByUsername(request.getUserPrincipal().getName()).orElseThrow();
            try {
                User user = userService.findById(id).orElseThrow();
                List<Route> favs = user.getFavoritos();
                List<RouteDTO> list = new ArrayList<RouteDTO>();
                for (Route fav : favs) {
                    list.add(new RouteDTO(fav));
                }
                return new ResponseEntity<>(list, HttpStatus.OK);
            } catch (NoSuchElementException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Operation(summary = "Get my favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "favourites getted sucessfully", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "array", implementation = RouteDTO.class))) }),
            @ApiResponse(responseCode = "401", description = "user is not registered", content = @Content),
            @ApiResponse(responseCode = "403", description = "not enough privileges", content = @Content),
            @ApiResponse(responseCode = "404", description = "user not found", content = @Content)

    })
    @GetMapping("/me/favorites")
    private ResponseEntity<List<RouteDTO>> getMyFavorites(HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            User userPrincipal = userService.findByUsername(request.getUserPrincipal().getName()).orElseThrow();
            List<Route> favs = userPrincipal.getFavoritos();
            List<RouteDTO> list = new ArrayList<RouteDTO>();
            for (Route fav : favs) {
                list.add(new RouteDTO(fav));
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Operation(summary = "Add route to current user's favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "route added sucessfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RouteDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "invalid route id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "user is not registered", content = @Content),
            @ApiResponse(responseCode = "404", description = "route not found", content = @Content)

    })
    @PostMapping("/me/favorites/{id}")
    private ResponseEntity<RouteDTO> addFavorites(@PathVariable long id, HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {

            Optional<User> userOp = userService.findByUsername(request.getUserPrincipal().getName());
            if (userOp.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            User userPrincipal = userOp.get();
            Optional<Route> routeop = routeService.findById(id);
            if (routeop.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Route route = routeop.get();
            userPrincipal.addFavoritos(route);
            userService.save(userPrincipal);
            return new ResponseEntity<>(new RouteDTO(route), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Operation(summary = "Remove route from current user's favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "route removed sucessfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RouteDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "invalid route id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "user is not registered", content = @Content),
            @ApiResponse(responseCode = "404", description = "route not found", content = @Content)

    })
    @DeleteMapping("/me/favorites/{id}")
    private ResponseEntity<RouteDTO> removeFavorites(@PathVariable long id, HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            User userPrincipal = userService.findByUsername(request.getUserPrincipal().getName()).orElseThrow();
            try {
                Route route = routeService.findById(id).orElseThrow();
                userPrincipal.removeFavoritos(route);
                userService.save(userPrincipal);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (NoSuchElementException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Operation(summary = "Get users list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users correct", content = @Content)

    })
    @GetMapping("/all") /// simplemente barra
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> findAll = userService.findAll();
        List<UserDTO> dtos = new ArrayList<>();
        for (User user : findAll) {
            dtos.add(new UserDTO(user));
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    private Route clearRoute(Route route) {
        List<User> users = userService.findAll();
        for (User user : users) {
            if (user.isInFavorites(route)) {
                user.removeFavoritos(route);
                userService.save(user);
            }

        }
        List<Comment> comments = route.getComments();
        for (Comment comment : comments) {
            comment.clear();
            commentService.save(comment);
            commentService.deleteById(comment.getId());
        }
        route.clear();
        routeService.save(route);
        return route;
    }
}
