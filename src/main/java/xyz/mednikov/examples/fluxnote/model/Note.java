package xyz.mednikov.examples.fluxnote.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notes")
public record Note(@Id String id, String content, String userId) {
    
}
