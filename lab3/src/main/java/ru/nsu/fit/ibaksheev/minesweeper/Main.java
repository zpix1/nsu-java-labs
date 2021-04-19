package ru.nsu.fit.ibaksheev.minesweeper;

import ru.nsu.fit.ibaksheev.minesweeper.controller.OnlineGameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.server.Server;
import ru.nsu.fit.ibaksheev.minesweeper.view.View;
import ru.nsu.fit.ibaksheev.minesweeper.view.gui.OnlineGameGUIView;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String addr = "localhost";
        int port = 5000;

        if (args.length == 2) {
            addr = args[0];
            port = Integer.parseInt(args[1]);
        }

        if (addr.equals("server")) {
            System.out.println("Server mode...");
            try {
                Server.main(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Console mode...");
            var controller = new OnlineGameController(addr, port);
            View<GameData> view = new OnlineGameGUIView(controller);
            view.start();
        }

    }
}
