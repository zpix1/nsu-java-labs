package ru.nsu.fit.ibaksheev.minesweeper.server;

import com.google.gson.Gson;
import ru.nsu.fit.ibaksheev.minesweeper.controller.OnlineGameController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Client {
    public interface ClientListenCallback {
        void callback(OnlineGameController.Message message);
    }

    private final Socket socket;
    private final PrintWriter socketOut;
    private final BufferedReader socketIn;
    private final BlockingQueue<OnlineGameController.Message> queue = new LinkedBlockingDeque<>();
    private final Gson gson = new Gson();

    private Thread listenThread;
    private Thread sendThread;


    public Client(Socket socket) throws IOException {
        this.socket = socket;
        socketOut = new PrintWriter(socket.getOutputStream(), true);
        socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        send();
    }

    public void dispose() {
        sendMessage(new OnlineGameController.Message("disconnect", 0, 0, null));

        listenThread.stop();
        sendThread.stop();
    }

    public void listen(ClientListenCallback callback) {
        listenThread = new Thread(() -> {
            while (true) {
                try {
                    var message = readMessage();
                    if (message.getEvent().equals("disconnect")) {
                        // Deprecated but who cares?
                        dispose();
                        return;
                    }
                    callback.callback(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        listenThread.start();
    }

    private void send() {
        sendThread = new Thread(() -> {
            while (true) {
                try {
                    var message = queue.take();
                    socketOut.println(gson.toJson(message));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        sendThread.start();
    }

    private OnlineGameController.Message readMessage() throws IOException {
        var messageStr = socketIn.readLine();
        return gson.fromJson(messageStr, OnlineGameController.Message.class);
    }

    public void sendMessage(OnlineGameController.Message message) {
        queue.add(message);
    }
}
