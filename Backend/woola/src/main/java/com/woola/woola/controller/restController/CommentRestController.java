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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.woola.woola.model.Comment;
import com.woola.woola.service.CommentService;
import com.woola.woola.model.Event;
import com.woola.woola.model.User;
import com.woola.woola.model.restModel.CommentDTO;
import com.woola.woola.service.EventService;
import com.woola.woola.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
//@RequestMapping("/api/comments")
public class CommentRestController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;
    
    
    @Operation(summary = "Get Comment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "comment obtained sucessfully",content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))}),
        @ApiResponse(responseCode = "400", description = "invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "401", description = "comment is not created", content = @Content),
        @ApiResponse(responseCode = "403", description = "not enough privileges or admin is modifying itself", content = @Content),
        @ApiResponse(responseCode = "404", description = "comment not found", content = @Content)
            
    })

    @GetMapping("/api/comment/{id}")//tested
    public ResponseEntity<CommentDTO> getComment(@PathVariable long id) {
        Optional<Comment> comment = commentService.findById(id);
        if (comment.isPresent()) {
            return new ResponseEntity<>(new CommentDTO(comment.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Post Comment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "comment posted sucessfully",content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))}),
        @ApiResponse(responseCode = "400", description = "invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "401", description = "comment is not created", content = @Content),
        @ApiResponse(responseCode = "403", description = "not enough privileges or admin is modifying itself", content = @Content),
        @ApiResponse(responseCode = "404", description = "comment not found", content = @Content)
            
    })

    @PostMapping("/api/comment/new/{id}")//tested
	public ResponseEntity<CommentDTO> postComment(@PathVariable long id, @RequestBody Comment newComent, HttpServletRequest request) throws URISyntaxException{
        System.out.println("crear");
        Principal principal = request.getUserPrincipal();
        if(principal != null){
            try {
                String fecha = DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a").format(LocalDateTime.now());
                newComent.setTime(fecha.toString());
                newComent.setCommentUser(principal.getName());
                commentService.save(newComent);
            
                Event event = eventService.findById(id).orElseThrow();
                event.addComment(newComent);
                eventService.save(event);
                URI location = new URI("https://127.0.0.1:8443/api/comments/"+newComent.getId());
                return ResponseEntity.created(location).body(new CommentDTO(newComent));
            } catch (NoSuchElementException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } 
	}

    @Operation(summary = "Remove Comment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "comment removed sucessfully",content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))}),
        @ApiResponse(responseCode = "400", description = "invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "401", description = "comment is not created", content = @Content),
        @ApiResponse(responseCode = "403", description = "not enough privileges or admin is modifying itself", content = @Content),
        @ApiResponse(responseCode = "404", description = "comment not found", content = @Content)
            
    })

    @DeleteMapping("/api/comment/{id}")//tested
	public ResponseEntity<CommentDTO> deleteComment(@PathVariable long id, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        System.out.println(user.getRol());
        if(!user.getRol().equals("ADMIN")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(commentService.findById(id).isPresent()){
            commentService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}
    @Operation(summary = "Get list of comments of event")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "list of comments sucessfully getted",content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "array", implementation = CommentDTO.class)))}),
        @ApiResponse(responseCode = "404", description = "event not found", content = @Content)      
    })
    @GetMapping("/api/comments/{id}")
    public ResponseEntity<List<CommentDTO>> deleteComment(@PathVariable long id){
        Optional<Event> eventOp=eventService.findById(id);
        if(eventOp.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Event event = eventOp.get();
        List<CommentDTO> list = new ArrayList<CommentDTO>();
        for(Comment comment:event.getComments()){
            list.add(new CommentDTO(comment));
        }
        return new ResponseEntity<>(list,HttpStatus.OK);
        
    }


    @Operation(summary = "Give like to comment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "like done succesfully",content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))}),
        @ApiResponse(responseCode = "401", description = "no user register", content = @Content),
        @ApiResponse(responseCode = "404", description = "comment not found", content = @Content)
            
    })
    @PostMapping("/api/comment/like/{id}")
    public ResponseEntity<Comment> giveDislike(@PathVariable long id, HttpServletRequest request){
        Optional<Comment> commentOp = commentService.findById(id);
        if(commentOp.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Comment comment = commentOp.get();

        Principal principal = request.getUserPrincipal();
        if(principal==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOp = userService.findByUsername(principal.getName());
        if(userOp.isEmpty()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userOp.get();
        comment.addFavorites(user);
        commentService.save(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}