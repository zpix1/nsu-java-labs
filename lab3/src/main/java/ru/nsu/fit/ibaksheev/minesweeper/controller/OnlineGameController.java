package ru.nsu.fit.ibaksheev.minesweeper.controller;

import lombok.Data;
import lombok.Getter;
import ru.nsu.fit.ibaksheev.minesweeper.model.*;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class OnlineGameController implements GameController {
    @Getter
    private GameModel syncModel;
    @Getter
    private GameModel model;

    public enum NetworkState {
        CONNECTION,
        WAITING_FOR_PLAYER,
        CONNECTED,
        DISCONNECTED,
        ERROR
    }

    @Getter
    private Model<NetworkState> networkModel;

    private Socket socket;
    private PrintWriter socketOut;
    private BufferedReader socketIn;

    public OnlineGameController() {
        syncModel = new GameModel();
        syncModel.setGameData(new GameData(new SettingsManager.Settings(10, 10, 10)));

        model = new GameModel();
        model.setGameData(new GameData(new SettingsManager.Settings(10, 10, 10)));

        model.subscribe(new ModelSubscriber<GameData>() {
            @Override
            public void modelChanged(Model<GameData> model) {
                System.out.println("transfer it to network");
            }
        }, "fieldUpdate");

        networkModel = new Model<>(NetworkState.CONNECTION);
    }

    @Override
    public void shoot(int x, int y) throws InvalidArgumentException {
        model.shoot(x, y);
    }

    @Override
    public void flag(int x, int y) throws InvalidArgumentException {
        model.flag(x, y);
    }

    public void connect() {
        System.out.println("connecting...");

        try {
            // TODO: Add to props
            socket = new Socket("localhost", 5000);
            socketOut = new PrintWriter(socket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            var response = socketIn.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            networkModel.setProperty(NetworkState.ERROR);
            return;
        }

        System.out.println("connected");
        networkModel.setProperty(NetworkState.CONNECTED);
    }
}
