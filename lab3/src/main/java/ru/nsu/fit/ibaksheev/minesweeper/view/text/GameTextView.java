package ru.nsu.fit.ibaksheev.minesweeper.view.text;

import ru.nsu.fit.ibaksheev.minesweeper.controller.GameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.*;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.GameException;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;
import ru.nsu.fit.ibaksheev.minesweeper.view.View;

import java.io.PrintStream;
import java.util.Scanner;

public class GameTextView implements View<GameData> {
    private final PrintStream out = System.out;
    private final Scanner in = new Scanner(System.in);

    private GameModel model;
    private GameController controller;

    private boolean lost = false;

    private char stateToChar(FieldCellState state) {
        if (state.type == FieldCellState.Type.Empty) {
            if (state.value == 0) {
                return ' ';
            }
            return Integer.toString(state.value).charAt(0);
        }
        if (state.type == FieldCellState.Type.Unknown) {
            return '.';
        }
        if (state.type == FieldCellState.Type.Flag) {
            return 'F';
        }
        return '?';
    }

    private void printField() {
        var field = model.getPlayerField();
        out.print(' ');
        for (int i = 0; i < field.length; i++) {
            out.print(i + 1);
        }
        out.println();
        for (int i = 0; i < field.length; i++) {
            // TODO: formatting (left align)
            out.print(i + 1);
            for (int j = 0; j < field[i].length; j++) {
                out.print(stateToChar(field[i][j]));
            }
            out.println();
        }
    }

    public GameTextView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
    }

    @Override
    public void start() {
        out.println("Welcome to MineSweeper!");

        controller.newGame(9, 9, 10);

        model.subscribe(model -> {
            // TODO: Is it okay?
            out.println("You lost!");
            lost = true;
        }, "lost");

        model.subscribe(model -> out.println("Whole field update!"), "wholeFieldUpdate");

        while (true) {
            printField();

            out.print("Enter command [S]hoot X Y, [F]lag X Y, [N]ew game, e[X]it: ");

            var line = in.next();

            if (line.startsWith("S")) {
                out.print("You shoot! Enter X Y: ");
                var x = in.nextInt();
                var y = in.nextInt();
                try {
                    controller.shot(x - 1, y - 1);
                } catch (InvalidArgumentException e) {
                    out.println("Invalid coordinates: " + x + " " + y);
                    continue;
                }
            } else if (line.startsWith("F")) {
                out.print("You flag! Enter X Y: ");
                var x = in.nextInt();
                var y = in.nextInt();
                try {
                    controller.flag(x - 1, y - 1);
                } catch (InvalidArgumentException e) {
                    out.println("Invalid coordinates: " + x + " " + y);
                    continue;
                }
            } else if (line.startsWith("N")) {
                controller.newGame(9, 9, 10);
            } else if (line.startsWith("X")) {
                break;
            }

            if (lost) {
                break;
            }
        }
    }
}
