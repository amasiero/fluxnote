package xyz.mednikov.examples.fluxnote.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
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
        return repository.findByUserId(userId).collectList().map(list -> new NoteList(list, list.size()));
    }

    @Override
    public Mono<NoteList> findAllNotesAndPaginate(String userId, int page, int limit) {
        // approach 1. data layer pagination
        // return repository.findByUserId(userId, PageRequest.of(page, limit)).map(list->new NoteList(list, 0));

        // approach 2. service layer pagination
        return repository.findByUserId(userId).collectList().map(list -> {
            int total = list.size();
            int start = (page - 1) * limit;
            List<Note> notes = list.stream().skip(start).limit(limit).collect(Collectors.toList());
            NoteList result = new NoteList(notes, total);
            return result;
        });
    }


    
}
