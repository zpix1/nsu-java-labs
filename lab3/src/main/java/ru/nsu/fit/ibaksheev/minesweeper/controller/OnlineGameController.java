package ru.nsu.fit.ibaksheev.minesweeper.controller;

import com.google.gson.Gson;
import lombok.*;
import ru.nsu.fit.ibaksheev.minesweeper.model.*;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

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
    private BlockingQueue<Message> messageQueue = new LinkedBlockingDeque<>();

    @Data
    @AllArgsConstructor
    public static class Message {
        private String event;
        private int x, y;
        private GameData gameData;
    }

    public OnlineGameController() {
        syncModel = new GameModel();
//        syncModel.setGameData(new GameData(new SettingsManager.Settings(10, 10, 10)));

        model = new GameModel();
//        model.setGameData(new GameData(new SettingsManager.Settings(10, 10, 10)));

//        model.subscribe(model -> System.out.println("transfer it to network"), "fieldUpdate");

        networkModel = new Model<>(NetworkState.CONNECTION);
    }

    @Override
    public void shoot(int x, int y) throws InvalidArgumentException {
        model.shoot(x, y);
        messageQueue.add(new Message("shoot", x, y, null));
    }

    @Override
    public void flag(int x, int y) throws InvalidArgumentException {
        model.flag(x, y);
        messageQueue.add(new Message("flag", x, y, null));
    }

    public void connect() {
        System.out.println("connecting...");

        try {
            // TODO: Add to props
            socket = new Socket("localhost", 5000);
            socketOut = new PrintWriter(socket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // wait until connected
            new Thread(() -> {
                var gson = new Gson();
                while (true) {
                    try {
                        var messageJson = socketIn.readLine();
                        var message = gson.fromJson(messageJson, Message.class);
                        System.out.println(message);
                        switch (message.event) {
                            case "wait":
                                networkModel.setProperty(NetworkState.WAITING_FOR_PLAYER);
                                break;
                            case "flag":
                                syncModel.flag(message.getX(), message.getY());
                                break;
                            case "shoot":
                                syncModel.shoot(message.getX(), message.getY());
                                break;
                            case "yourField":
                                model.setGameData(message.getGameData());

                                if (syncModel.propertyExists()) {
                                    networkModel.setProperty(NetworkState.CONNECTED);
                                }
                                break;
                            case "opponentField":
                                syncModel.setGameData(message.getGameData());
                                if (model.propertyExists()) {
                                    networkModel.setProperty(NetworkState.CONNECTED);
                                }
                                break;
                        }
                    } catch (IOException | InvalidArgumentException e) {
                        networkModel.setProperty(NetworkState.ERROR);
                        return;
                    }
                }
            }).start();
            new Thread(() -> {
                var gson = new Gson();
                while (true) {
                    Message message = null;
                    try {
                        message = messageQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (message != null) {
                        socketOut.println(gson.toJson(message));
                    }
                }
            }).start();

//            messageQueue.add(new Message("field", 0, 0, model.getProperty()));
        } catch (IOException e) {
            e.printStackTrace();
            networkModel.setProperty(NetworkState.ERROR);
        }
    }
}
