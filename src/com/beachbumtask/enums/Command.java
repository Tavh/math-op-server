package com.beachbumtask.enums;

public enum Command {
    ADDITION("add"),
    SUBTRACTION("subtract"),
    MULTIPLICATION("multiply"),
    DIVISION("divide");

    Command(String name) {
        this.commandString = name;
    }

    final private String commandString;

    public String getName() {
        return this.commandString;
    }
}
