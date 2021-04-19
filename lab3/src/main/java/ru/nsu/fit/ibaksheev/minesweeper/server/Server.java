package ru.nsu.fit.ibaksheev.minesweeper.server;

import ru.nsu.fit.ibaksheev.minesweeper.controller.OnlineGameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.SettingsManager;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(int port) throws IOException {
        var socket = new ServerSocket(port, 6);
        while (true) {
            var firstClient = new Client(socket.accept());
            firstClient.sendMessage(new OnlineGameController.Message("wait", 0,0,null));
            var secondClient =  new Client(socket.accept());

            var game = new GameModel();
            game.setGameData(new GameData(new SettingsManager.Settings(5, 5, 3)));
            game.pregenerate();

            firstClient.listen(secondClient::sendMessage);
            secondClient.listen(firstClient::sendMessage);

            firstClient.sendMessage(new OnlineGameController.Message("yourField", 0, 0, game.getProperty()));
            firstClient.sendMessage(new OnlineGameController.Message("opponentField", 0, 0, game.getProperty()));

            secondClient.sendMessage(new OnlineGameController.Message("yourField", 0, 0, game.getProperty()));
            secondClient.sendMessage(new OnlineGameController.Message("opponentField", 0, 0, game.getProperty()));
        }
    }
}
