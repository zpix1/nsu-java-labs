package ru.nsu.fit.ibaksheev.minesweeper.server;

import com.google.gson.Gson;
import ru.nsu.fit.ibaksheev.minesweeper.controller.OnlineGameController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Client {
    public interface ClientListenCallback {
        void callback(OnlineGameController.Message message);
    }

    private Socket socket;
    private PrintWriter socketOut;
    private BufferedReader socketIn;
    private BlockingQueue<OnlineGameController.Message> queue = new LinkedBlockingDeque<>();
    private Gson gson = new Gson();

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        socketOut = new PrintWriter(socket.getOutputStream(), true);
        socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        send();
    }

    public void listen(ClientListenCallback callback) {
        new Thread(() -> {
            while (true) {
                try {
                    var message = readMessage();
                    callback.callback(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void send() {
        new Thread(() -> {
            while (true) {
                try {
                    var message = queue.take();
                    socketOut.println(gson.toJson(message));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private OnlineGameController.Message readMessage() throws IOException {
        var messageStr = socketIn.readLine();
        return gson.fromJson(messageStr, OnlineGameController.Message.class);
    }

    public void sendMessage(OnlineGameController.Message message) {
        queue.add(message);
    }
}
