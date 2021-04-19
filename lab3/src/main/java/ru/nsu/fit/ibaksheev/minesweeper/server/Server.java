package ru.nsu.fit.ibaksheev.minesweeper.server;

import ru.nsu.fit.ibaksheev.minesweeper.controller.OnlineGameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main() throws IOException {
        var socket = new ServerSocket(5000, 2);
        while (true) {
            var firstClient = new Client(socket.accept());
            System.out.println("first connected");
//            var secondClient = new Client(socket.accept());
//            System.out.println("second connected");
            firstClient.sendMessage(new OnlineGameController.Message("connected", 0,0,null));
//            secondClient.sendMessage(new OnlineGameController.Message("connected", 0,0,null));
            var game = new GameModel();
//            firstClient.listen(secondClient::sendMessage);
//            secondClient.listen(firstClient::sendMessage);
            firstClient.sendMessage(new OnlineGameController.Message("field", 0, 0, game.getProperty()));
//            secondClient.sendMessage(new OnlineGameController.Message("field", 0, 0, game.getProperty()));
        }
    }
}
