package xyz.mednikov.examples.fluxnote.service;

import java.util.Optional;

import reactor.core.publisher.Mono;
import xyz.mednikov.examples.fluxnote.model.Note;
import xyz.mednikov.examples.fluxnote.model.NoteList;

public interface NoteService {
    
    Mono<Note> createNote (Note note);

    Mono<Note> updateNote (Note note);

    Mono<Void> removeNote (String noteId);

    Mono<Optional<Note>> findNote (String id);

    Mono<NoteList> findAllNotes(String userId);
}
