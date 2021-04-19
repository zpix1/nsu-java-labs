package ru.nsu.fit.ibaksheev.minesweeper.controller;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.Model;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class OnlineGameController implements GameController {
    final String addr;
    final int port;
    @Getter
    private final GameModel syncModel;
    @Getter
    private final GameModel model;
    @Getter
    private final Model<NetworkState> networkModel;
    private final BlockingQueue<Message> messageQueue = new LinkedBlockingDeque<>();
    private Socket socket;
    private PrintWriter socketOut;
    private BufferedReader socketIn;

    public OnlineGameController(String addr, int port) {
        this.addr = addr;
        this.port = port;
        syncModel = new GameModel();
//        syncModel.setGameData(new GameData(new SettingsManager.Settings(10, 10, 10)));

        model = new GameModel();

        networkModel = new Model<>(NetworkState.CONNECTION);
    }

    @Override
    public void flag(int x, int y) throws InvalidArgumentException {
        model.flag(x, y);
        messageQueue.add(new Message("flag", x, y, null));
    }

    @Override
    public void shoot(int x, int y) throws InvalidArgumentException {
        model.shoot(x, y);
        messageQueue.add(new Message("shoot", x, y, null));
    }

    private boolean alive() {
        return networkModel.getProperty() == NetworkState.CONNECTION || networkModel.getProperty() == NetworkState.WAITING_FOR_OPPONENT || networkModel.getProperty() == NetworkState.CONNECTED;
    }

    public void connect() {
        try {
            socket = new Socket(addr, port);
            socketOut = new PrintWriter(socket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // wait until connected
            var readThread = new Thread(() -> {
                var gson = new Gson();
                while (alive()) {
                    try {
                        var messageJson = socketIn.readLine();
                        var message = gson.fromJson(messageJson, Message.class);
                        System.out.println(message);
                        switch (message.event) {
                            case "disconnect":
                                networkModel.setProperty(NetworkState.DISCONNECTED);
                                break;
                            case "wait":
                                networkModel.setProperty(NetworkState.WAITING_FOR_OPPONENT);
                                break;
                            case "flag":
                                syncModel.flag(message.getX(), message.getY());
                                break;
                            case "shoot":
                                syncModel.shoot(message.getX(), message.getY());
                                break;
                            case "won":
                                networkModel.setProperty(NetworkState.OPPONENT_WON);
                                break;
                            case "lost":
                                networkModel.setProperty(NetworkState.OPPONENT_LOST);
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
                System.out.println("stopped");
            });
            readThread.setDaemon(true);
            readThread.start();
            var sendThread = new Thread(() -> {
                var gson = new Gson();
                while (alive()) {
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
                System.out.println("stopped");
            });
            sendThread.setDaemon(true);
            sendThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            networkModel.setProperty(NetworkState.ERROR);
        }
    }

    public void endGame() {
        if (networkModel.getProperty() == NetworkState.CONNECTED) {
            if (model.getState() == GameData.State.WON) {
                messageQueue.add(new Message("won", 0, 0, null));
            } else if (model.getState() == GameData.State.LOST) {
                messageQueue.add(new Message("lost", 0, 0, null));
            }
        }
        messageQueue.add(new Message("disconnect", 0, 0, null));
    }

    public enum NetworkState {
        CONNECTION,
        OPPONENT_WON,
        OPPONENT_LOST,
        WAITING_FOR_OPPONENT,
        CONNECTED,
        DISCONNECTED,
        ERROR
    }

    @Data
    @AllArgsConstructor
    public static class Message {
        private String event;
        private int x, y;
        private GameData gameData;
    }
}
