package ru.nsu.fit.ibaksheev.minesweeper.view.text;

import ru.nsu.fit.ibaksheev.minesweeper.controller.GameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.*;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;
import ru.nsu.fit.ibaksheev.minesweeper.view.View;

import java.io.PrintStream;
import java.util.Scanner;

public class GameTextView implements View<GameData> {
    private final PrintStream out = System.out;
    private final Scanner in = new Scanner(System.in);

    final private GameModel model;
    final private GameController controller;

    private boolean endGame = false;

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

    private void printField(FieldCellState[][] field) {
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

        controller.newGame(5, 5, 1);

        model.subscribe(model -> {
            // TODO: Is it okay?
            out.println("You lost!");
            endGame = true;
        }, "lost");

        model.subscribe(model -> {
            // TODO: Is it okay?
            out.println("You won! Congratulations!");
            endGame = true;
        }, "won");

        model.subscribe(model -> out.println("Whole field update!"), "wholeFieldUpdate");

        while (true) {
            out.println("Real field");
            printField(model.getRealField());
            out.println("Player field");
            printField(model.getPlayerField());

            out.print("Enter command [S]hoot Y X, [F]lag Y X, [N]ew game, e[X]it: ");

            var line = in.next();

            if (line.startsWith("S")) {
                out.println("You shoot! Enter Y X: ");
                var x = in.nextInt();
                var y = in.nextInt();
                try {
                    controller.shoot(x - 1, y - 1);
                } catch (InvalidArgumentException e) {
                    out.println("Invalid coordinates: " + x + " " + y);
                    continue;
                }
            } else if (line.startsWith("F")) {
                out.println("You flag! Enter X Y: ");
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

            if (endGame) {
                out.println("Thanks for playing a game!");
                controller.newGame(9, 9, 10);
            }
        }
    }
}
