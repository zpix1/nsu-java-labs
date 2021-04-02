package ru.nsu.fit.ibaksheev.minesweeper.controller;

import lombok.Getter;
import org.joda.time.Duration;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.model.SettingsGameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.ScoresManager;
import ru.nsu.fit.ibaksheev.minesweeper.model.SettingsManager;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

import java.io.*;
import java.util.Date;

public class LocalGameController {
    private static final String saveFilename = "save.bin";

    @Getter
    SettingsGameModel model;

    public LocalGameController() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(saveFilename))) {
            model = (SettingsGameModel) objectInputStream.readObject();
            System.out.println("Loaded model");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error while reading model, creating new one.");
            e.printStackTrace();
            model = new SettingsGameModel();
        }
    }

    public void dispose() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(saveFilename))) {
            objectOutputStream.writeObject(model);
        } catch (IOException e) {
            System.err.println("Can't save model, exiting anyway.");
            e.printStackTrace();
        }
    }

    public void shoot(int x, int y) throws InvalidArgumentException {
        model.shoot(x, y);
    }

    public void flag(int x, int y) throws InvalidArgumentException {
        model.flag(x, y);
    }

    public void loadOrStartNew(SettingsManager.Settings settings) {
        if (!model.propertyExists()) {
            newGameWithSettings(settings);
        }
    }

    public void newGameWithSettings(SettingsManager.Settings settings) {
        var data = new GameData(settings);
        model.setGameData(data);
    }

    public void endGame() {
        if (model.getState() == GameData.State.WON) {
            var duration = new Duration(model.getStartedAt().getTime(), new Date().getTime());
            model.getScoresManager().addEntry(new ScoresManager.ScoreEntry(model.getSettings(), duration));
        }
    }
}
