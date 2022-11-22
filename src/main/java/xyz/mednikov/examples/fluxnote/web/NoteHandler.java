package xyz.mednikov.examples.fluxnote.web;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import xyz.mednikov.examples.fluxnote.model.Note;
import xyz.mednikov.examples.fluxnote.service.NoteService;

@Component
public record NoteHandler(NoteService service) {
    
    Mono<ServerResponse> createNote(ServerRequest request){
        return request.bodyToMono(Note.class)
        .flatMap(n -> service.createNote(n))
        .flatMap(result -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(result))
        .onErrorResume(err -> ServerResponse.badRequest().build());
    }

    Mono<ServerResponse> updateNote (ServerRequest request){
        return request.bodyToMono(Note.class)
        .flatMap(n -> service.updateNote(n))
        .flatMap(result -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(result))
        .onErrorResume(err -> ServerResponse.badRequest().build());
    }

    Mono<ServerResponse> findNote (ServerRequest request){
        String id = request.pathVariable("id");
        return service.findNote(id)
        .flatMap(result -> {
            if (result.isPresent()){
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(result);
            } else {
                return ServerResponse.notFound().build();
            }
        });
    }

    Mono<ServerResponse> removeNote (ServerRequest request){
        String id = request.pathVariable("id");
        return service.removeNote(id).flatMap(result -> ServerResponse.ok().build())
        .onErrorResume(err -> ServerResponse.badRequest().build());
    }

    Mono<ServerResponse> findAllNotes (ServerRequest request){
        String userId = request.pathVariable("userId");
        return service.findAllNotes(userId).flatMap(result -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(result));
    }

    Mono<ServerResponse> findAllNotesAndPaginate(ServerRequest request){
        String userId = request.pathVariable("userId");
        Optional<Integer> page = request.queryParam("page").map(r -> Integer.valueOf(r));
        Optional<Integer> limit = request.queryParam("limit").map(r->Integer.valueOf(r));
        if (page.isPresent() && limit.isPresent()) {
            // findAllNotesAndPaginate
            return service.findAllNotesAndPaginate(userId, page.get(), limit.get()).flatMap(result -> ServerResponse.ok().header("X-APP-PAGINATION-ENABLED","true").contentType(MediaType.APPLICATION_JSON).bodyValue(result));
        } else {
            // findAllNotes
            return service.findAllNotes(userId).flatMap(result -> ServerResponse.ok().header("X-APP-PAGINATION-ENABLED","false").contentType(MediaType.APPLICATION_JSON).bodyValue(result));
        }
    }
}
