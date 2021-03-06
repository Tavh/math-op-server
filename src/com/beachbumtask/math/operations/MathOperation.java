package com.beachbumtask.math.operations;

import com.beachbumtask.enums.Command;

/**
 * A math operation abstract class that implements a perform method that parses commands from the user
 * it's members should implement the actual calculation of the inputs from the user
 */
public abstract class MathOperation {
    final public double perform(String unparsedCommand) {
        StringBuilder firstNumStr = new StringBuilder();
        StringBuilder secondNumStr = new StringBuilder();
        boolean isAtFirstNum = true;
        for (int i = getFirstNumberIndex(); i < unparsedCommand.length(); i++) {
            char c = unparsedCommand.charAt(i);
            if (Character.isDigit(c) || c == '.' || c == '-') {
                if (isAtFirstNum) {
                    firstNumStr.append(c);
                } else {
                    secondNumStr.append(c);
                }
            } else {
                i++;
                isAtFirstNum = false;
            }
        }
        return parseAndCalc(firstNumStr.toString(), secondNumStr.toString());
    }

    private int getFirstNumberIndex() {
        return getCommand().getName().length() + 2;
    }

    protected abstract Command getCommand();
    protected abstract double parseAndCalc(String n1, String n2);
}
