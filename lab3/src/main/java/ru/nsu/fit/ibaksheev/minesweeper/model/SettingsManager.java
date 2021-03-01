package ru.nsu.fit.ibaksheev.minesweeper.model;

import ru.nsu.fit.ibaksheev.minesweeper.model.exceptions.InvalidArgumentException;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class SettingsManager {
    private final SortedMap<String, Settings> settingsTypes;

    // TODO: Tried to use reflexion api here, but it's just evil
    public SettingsManager() {
        var filename = "/settings.properties";

        Properties props = new Properties();
        settingsTypes = new TreeMap<>();
        try {
            props.load(this.getClass().getResourceAsStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (var propName : Collections.list(props.keys()).stream().sorted(Comparator.comparing(Object::toString)).toArray()) {
            var splitProp = propName.toString().split("\\.");
            var type = splitProp[0];
            var fieldName = splitProp[1];
            if (settingsTypes.get(type) == null) {
                settingsTypes.put(type, new Settings(0, 0, 0));
            }
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

    public Settings getSettingsByName(String name) throws InvalidArgumentException {
        if (!settingsTypes.containsKey(name)) {
            throw new InvalidArgumentException("settings not found by name " + name);
        }
        return settingsTypes.get(name);
    }

    public Set<Map.Entry<String, Settings>> getSettingsList() {
        return settingsTypes.entrySet();
    }

    public Settings getFirstSettings() {
        return settingsTypes.get(settingsTypes.firstKey());
    }

    public static class Settings implements Serializable {
        private int width;
        private int height;
        private int minesCount;
        private String name;

        private Settings(int width, int height, int minesCount) {
            this.width = width;
            this.height = height;
            this.minesCount = minesCount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getMinesCount() {
            return minesCount;
        }

        public void setMinesCount(int minesCount) {
            this.minesCount = minesCount;
        }
    }
}
