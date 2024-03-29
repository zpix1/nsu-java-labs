package ru.nsu.fit.ibaksheev.minesweeper.view.text;

import ru.nsu.fit.ibaksheev.minesweeper.controller.LocalGameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.FieldCellState;
import ru.nsu.fit.ibaksheev.minesweeper.model.SettingsGameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.SettingsManager;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;
import ru.nsu.fit.ibaksheev.minesweeper.view.GameView;

import java.io.PrintStream;
import java.util.Scanner;

public class GameTextView implements GameView {
    private final PrintStream out = System.out;
    private final Scanner in = new Scanner(System.in);

    final SettingsManager settingsManager = new SettingsManager();
    private final SettingsGameModel model;

    private boolean endGame = false;

    private final LocalGameController controller;

    public GameTextView(LocalGameController controller) {
        this.controller = controller;
        this.model = controller.getModel();
    }

    @Override
    public void start() {
        out.println("Welcome to MineSweeper!");

        controller.newGameWithSettings(settingsManager.getFirstSettings());

        model.subscribe(model -> {
            out.println("You lost!");
            endGame = true;
        }, "lost");

        model.subscribe(model -> {
            out.println("You won! Congratulations!");
            endGame = true;
        }, "won");

        model.subscribe(model -> out.println("Whole field update!"), "wholeFieldUpdate");
        model.subscribe(model -> out.println("Reset!"), "reset");


        while (true) {
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
                controller.newGameWithSettings(settingsManager.getFirstSettings());
            } else if (line.startsWith("X")) {
                break;
            }

            if (endGame) {
                out.println("Thanks for playing a game!");
                controller.newGameWithSettings(settingsManager.getFirstSettings());
            }
        }
    }

    private void printField(FieldCellState[][] field) {
        out.print(' ');
        for (int i = 0; i < field[0].length; i++) {
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

    private char stateToChar(FieldCellState state) {
        if (state.getType() == FieldCellState.Type.Empty) {
            if (state.getValue() == 0) {
                return ' ';
            }
            return Integer.toString(state.getValue()).charAt(0);
        }
        if (state.getType() == FieldCellState.Type.Unknown) {
            return '.';
        }
        if (state.getType() == FieldCellState.Type.Flag) {
            return 'F';
        }
        return '?';
    }
}
