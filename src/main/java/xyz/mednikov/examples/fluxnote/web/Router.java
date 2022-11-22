package xyz.mednikov.examples.fluxnote.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class Router {
    
    @Bean
    RouterFunction<ServerResponse> notesEndpoint(NoteHandler handler){
        return RouterFunctions
        .route(POST("/notes").and(accept(MediaType.APPLICATION_JSON)), handler::createNote)
        .andRoute(PUT("/notes").and(accept(MediaType.APPLICATION_JSON)), handler::updateNote)
        .andRoute(DELETE("/note/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::removeNote)
        .andRoute(GET("/note/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findNote)
        .andRoute(GET("/notes/{userId}").and(accept(MediaType.APPLICATION_JSON)), handler::findAllNotesAndPaginate);
    }
}
