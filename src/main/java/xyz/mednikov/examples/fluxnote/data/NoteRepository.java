package xyz.mednikov.examples.fluxnote.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import xyz.mednikov.examples.fluxnote.model.Note;

public interface NoteRepository extends ReactiveCrudRepository<Note, String> {
    
    Flux<Note> findByUserId (String userId);
}
