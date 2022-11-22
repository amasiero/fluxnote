package xyz.mednikov.examples.fluxnote.model;

import java.util.List;

public record NoteList(List<Note> notes, int total) {
    
}
