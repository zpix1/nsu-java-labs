package ru.nsu.fit.ibaksheev;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Counter<T> {
    private int total;
    private final Map<T, Integer> stats = new HashMap<>();

    public void add(T value) {
        total++;
        stats.merge(value, 1, Integer::sum);
    }

    public List<Map.Entry<T, Integer>> mostCommon() {
        return this.stats
                .entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue())).collect(Collectors.toList());
    }

    public int getTotal() {
        return total;
    }
}
