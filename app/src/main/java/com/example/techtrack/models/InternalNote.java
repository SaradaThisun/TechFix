package com.example.techtrack.models;

public class InternalNote {
    private String id;
    private String timestamp;
    private String note;
    private String author;

    public InternalNote() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}