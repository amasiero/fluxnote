package xyz.mednikov.examples.fluxnote.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import xyz.mednikov.examples.fluxnote.data.NoteRepository;
import xyz.mednikov.examples.fluxnote.model.Note;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {
    
    @Mock
    NoteRepository repository;

    @InjectMocks
    NoteServiceImpl service;

    @Test
    void createNoteTest(){
        Note note = new Note(null, "New note", "user");
        Mockito.when(repository.save(note)).thenReturn(Mono.just(note));
        StepVerifier.create(service.createNote(note))
        .assertNext(result -> Assertions.assertThat(result).isNotNull())
        .verifyComplete();
    }

    @Test
    void updateNoteTest(){
        Note note = new Note("id", "New note", "user");
        Mockito.when(repository.save(note)).thenReturn(Mono.just(note));
        StepVerifier.create(service.updateNote(note))
        .assertNext(result -> Assertions.assertThat(result).isNotNull())
        .verifyComplete();
    }

    @Test
    void findNoteByIdExistsTest(){
        Note note = new Note("id", "New note", "user");
        Mockito.when(repository.findById("id")).thenReturn(Mono.just(note));
        StepVerifier.create(service.findNote("id"))
        .assertNext(result -> Assertions.assertThat(result).isPresent())
        .verifyComplete();
    }

    @Test
    void findNoteByIdDoesNotExistTest(){
        Mockito.when(repository.findById("id")).thenReturn(Mono.empty());
        StepVerifier.create(service.findNote("id"))
        .assertNext(result -> Assertions.assertThat(result).isEmpty())
        .verifyComplete();
    }

    @Test
    void findNotesTest(){
        Flux<Note> notes = Flux.just(
            new Note("id", "New note", "user"),
            new Note("id", "New note", "user"),
            new Note("id", "New note", "user"),
            new Note("id", "New note", "user"),
            new Note("id", "New note", "user")
        );
        Mockito.when(repository.findByUserId("user")).thenReturn(notes);
        StepVerifier.create(service.findAllNotes("user"))
        .assertNext(result -> Assertions.assertThat(result.notes()).hasSize(5))
        .verifyComplete();
    }
}
