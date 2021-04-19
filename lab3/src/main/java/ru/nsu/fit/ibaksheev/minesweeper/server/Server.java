package ru.nsu.fit.ibaksheev.minesweeper.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main() throws IOException {
        var socket = new ServerSocket(5000, 2);
        while (true) {
            var firstClient = socket.accept();
            var secondClient = socket.accept();

        }
    }
}
