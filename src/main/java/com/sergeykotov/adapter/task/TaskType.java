package com.sergeykotov.adapter.task;

public enum TaskType {
    CREATE_RULE("create rule"),
    UPDATE_RULE("update rule"),
    DELETE_RULE("delete rule"),
    RESTORE_INTEGRITY("restore integrity");

    TaskType(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}