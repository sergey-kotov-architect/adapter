package com.sergeykotov.adapter.dto;

public class TaskResultsDeletion {
    private boolean deleted;
    private int count;
    private String note;

    public TaskResultsDeletion() {
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}