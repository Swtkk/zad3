package com.example.todo;

import android.util.Log;

import com.example.todo.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage instance = new TaskStorage();
    private final List<Task> tasks;

    private TaskStorage() {
        tasks = new ArrayList<>();
        for (int i = 1; i <= 150; i++) {
            Task task = new Task();
            task.setName("Zadanie " + i);
            task.setDone(i % 3 ==0);
            tasks.add(task);
        }
        Log.d("TaskStorage", "Initialized " + tasks.size() + " tasks.");
    }

    public static TaskStorage getInstance() {
        return instance;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(UUID id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null; // Zwraca null, jeśli zadanie nie zostało znalezione
    }
}
