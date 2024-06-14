package com.example.todoapp;

public class ToDo {
    private int id;
    private String task;
    private String description;
    private String dueTime;
    private boolean completed;

    public ToDo() {}

    public ToDo(int id, String task, String description, String dueTime, boolean completed) {
        this.id = id;
        this.task = task;
        this.description = description;
        this.dueTime = dueTime;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }

}


