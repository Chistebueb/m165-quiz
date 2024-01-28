package org.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Player {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty score;
    private final SimpleDoubleProperty time;

    private static int currentRank = 1;
    private SimpleIntegerProperty rank;

    public Player(String name, Integer score, Double time) {
        this.name = new SimpleStringProperty(name);
        this.score = new SimpleIntegerProperty(score);
        this.time = new SimpleDoubleProperty(time);
        this.rank = new SimpleIntegerProperty(currentRank);
        currentRank++;
    }
}
