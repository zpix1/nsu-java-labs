package ru.nsu.fit.ibaksheev.minesweeper.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter socketOut;
    private BufferedReader socketIn;

    Client(Socket socket) throws IOException {
        this.socket = socket;
        socketOut = new PrintWriter(socket.getOutputStream(), true);
        socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}
