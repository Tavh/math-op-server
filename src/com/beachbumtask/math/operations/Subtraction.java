package com.beachbumtask.math.operations;

import com.beachbumtask.enums.Command;

public class Subtraction extends MathOperation {

    @Override
    protected Command getCommand() {
        return Command.SUBTRACTION;
    }

    @Override
    protected double parseAndCalc(String n1, String n2) {
        return Double.parseDouble(n1) - Double.parseDouble(n2);
    }
}
