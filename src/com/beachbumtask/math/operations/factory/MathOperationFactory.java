package com.beachbumtask.math.operations.factory;

import com.beachbumtask.enums.Command;
import com.beachbumtask.math.operations.*;

public class MathOperationFactory {
    private MathOperationFactory() {}

    private static MathOperationFactory instance = new MathOperationFactory();

    public static MathOperationFactory getOperationsFactory() {
        if (instance == null) {
            synchronized (MathOperationFactory.class) {
                if (instance == null) {
                    instance = new MathOperationFactory();
                }
            }
        }
        return instance;
    }

    public MathOperation getOperation(final String unparsedCommand) {
        if (unparsedCommand.contains(Command.ADDITION.getName())) {
            return new Addition();
        }

        if (unparsedCommand.contains(Command.SUBTRACTION.getName())) {
            return new Subtraction();
        }

        if (unparsedCommand.contains(Command.MULTIPLICATION.getName())) {
            return new Multiplication();
        }


        if (unparsedCommand.contains(Command.DIVISION.getName())) {
            return new Division();
        }

        throw new RuntimeException("Could not parse operation command");
    }

}
