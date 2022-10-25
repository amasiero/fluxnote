package xyz.mednikov.examples.fluxnote.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import xyz.mednikov.examples.fluxnote.data.NoteRepository;
import xyz.mednikov.examples.fluxnote.model.Note;
import xyz.mednikov.examples.fluxnote.model.NoteList;

@Component
public record NoteServiceImpl(NoteRepository repository) implements NoteService {

    @Override
    public Mono<Note> createNote(Note note) {
        return repository.save(note);
    }

    @Override
    public Mono<Note> updateNote(Note note) {
        return repository.save(note);
    }

    @Override
    public Mono<Void> removeNote(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Optional<Note>> findNote(String id) {
        return repository.findById(id).map(note -> Optional.ofNullable(note)).defaultIfEmpty(Optional.empty());
    }

    @Override
    public Mono<NoteList> findAllNotes(String userId) {
        return repository.findByUserId(userId).collectList().map(list -> new NoteList(list));
    }


    
}
