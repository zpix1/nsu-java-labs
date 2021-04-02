package ru.nsu.fit.ibaksheev.minesweeper.view;

import ru.nsu.fit.ibaksheev.minesweeper.controller.GameController;
import ru.nsu.fit.ibaksheev.minesweeper.controller.LocalGameController;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameData;
import ru.nsu.fit.ibaksheev.minesweeper.model.GameModel;
import ru.nsu.fit.ibaksheev.minesweeper.model.SettingsGameModel;

public interface GameView extends View<GameData> {
   void start();
}
