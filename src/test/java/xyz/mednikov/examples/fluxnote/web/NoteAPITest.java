package xyz.mednikov.examples.fluxnote.web;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;
import xyz.mednikov.examples.fluxnote.model.Note;
import xyz.mednikov.examples.fluxnote.model.NoteList;
import xyz.mednikov.examples.fluxnote.service.NoteService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {NoteHandler.class, Router.class})
@WebFluxTest
class NoteAPITest {
    
    @Autowired ApplicationContext context;
    @MockBean NoteService service;

    private WebTestClient client;

    @BeforeEach
    void setup(){
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void createNoteTest(){
        Note body = new Note(null, "content", "userId");
        Note result = new Note("id", "content", "user");
        Mockito.when(service.createNote(body)).thenReturn(Mono.just(result));
        client.post()
        .uri(URI.create("/notes"))
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(body), Note.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Note.class)
        .value(r -> Assertions.assertThat(r).isNotNull());
    }

    @Test
    void updateNoteTest(){
        Note body = new Note("id", "content", "user");
        Mockito.when(service.updateNote(body)).thenReturn(Mono.just(body));
        client.put()
        .uri(URI.create("/notes"))
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(body), Note.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Note.class)
        .value(r -> Assertions.assertThat(r).isNotNull());
    }

    @Test
    void findNoteByIdExistsTest(){
        String noteId = "id";
        Note result = new Note("id", "content", "user");
        Mockito.when(service.findNote(noteId)).thenReturn(Mono.just(Optional.of(result)));
        client.get()
        .uri(URI.create("/note/id"))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Note.class)
        .value(r -> Assertions.assertThat(r).isNotNull());
    }

    @Test
    void findNoteByIdDoesNotExistTest(){
        String noteId = "id";
        Mockito.when(service.findNote(noteId)).thenReturn(Mono.just(Optional.empty()));
        client.get()
        .uri(URI.create("/note/id"))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
    }

    @Test
    void findAllNotesTest(){
        String userId = "user";
        List<Note> notes = List.of(
            new Note("id", "content", "user"),
            new Note("id", "content", "user"),
            new Note("id", "content", "user"),
            new Note("id", "content", "user"),
            new Note("id", "content", "user")
        );
        NoteList result = new NoteList(notes);
        Mockito.when(service.findAllNotes(userId)).thenReturn(Mono.just(result));
        client.get()
        .uri(URI.create("/notes/user"))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(NoteList.class)
        .value(r -> Assertions.assertThat(r.notes()).hasSameElementsAs(notes));
    }
}
