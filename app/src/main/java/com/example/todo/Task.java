package com.example.todo;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;
    Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Task() {
        id = UUID.randomUUID();
        date = new Date();
        category = Category.Dom;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public boolean isDone() {
        return done;
    }

    // Gettery i settery
    // ...
}

