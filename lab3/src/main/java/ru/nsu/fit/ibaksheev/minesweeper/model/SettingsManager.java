package ru.nsu.fit.ibaksheev.minesweeper.model;

import com.sun.source.tree.Tree;
import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SettingsManager {
    private final SortedMap<String, GameData.Settings> settingsTypes;

    // TODO: Tried to use reflexion api here, but it's just evil

    public SettingsManager(String filename) {
        Properties props = new Properties();
        settingsTypes = new TreeMap<>();
        try {
            props.load(this.getClass().getResourceAsStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (var propName: Collections.list(props.keys()).stream().sorted(Comparator.comparing(Object::toString)).toArray()) {
            var splitProp = propName.toString().split("\\.");
            var type = splitProp[0];
            var fieldName = splitProp[1];
            if (settingsTypes.get(type) == null) {
                settingsTypes.put(type, new GameData.Settings(0, 0, 0));
            }
            System.out.println(propName);
            switch (fieldName) {
                case "name":
                    settingsTypes.get(type).setName((String) props.get(propName));
                    break;
                case "width":
                    settingsTypes.get(type).setWidth(Integer.parseInt((String) props.get(propName)));
                    break;
                case "height":
                    settingsTypes.get(type).setHeight(Integer.parseInt((String) props.get(propName)));
                    break;
                case "minesCount":
                    settingsTypes.get(type).setMinesCount(Integer.parseInt((String) props.get(propName)));
                    break;
            }
        }
    }

    public GameData.Settings getSettingsByName(String name) throws InvalidArgumentException {
        if (!settingsTypes.containsKey(name)) {
            throw new InvalidArgumentException("settings not found by name " + name);
        }
        return settingsTypes.get(name);
    }

    public Set<Map.Entry<String, GameData.Settings>> getSettingsList() {
        return settingsTypes.entrySet();
    }
}
