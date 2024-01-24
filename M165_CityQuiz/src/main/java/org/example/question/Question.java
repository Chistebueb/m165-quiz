package org.example.question;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.bson.Document;
import org.example.App;
import org.example.GameLoop;
import org.example.DBConnector;
import org.example.JavaFX;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Question {

    private StackPane root;
    private Map<String, Button> cityButtons;
    private List<Document> cities;
    private GameLoop gl;

    public Question(String question, QuestionType questionType, String category) {
        gl = App.getGL();
        root = JavaFX.root;
        root.getChildren().clear();
        root.getStyleClass().add("border");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Text livesDisplay = new Text(getLivesAsHearts(gl.getHeartCount()));
        livesDisplay.getStyleClass().add("lives-display");
        layout.getChildren().add(livesDisplay);

        Text title = new Text(question);
        title.getStyleClass().add("question-title");
        layout.getChildren().add(title);

        int numberOfCities = (questionType == QuestionType.MORE || questionType == QuestionType.LESS) ? 2 : 4;
        cities = App.getDb().getRandomCities(numberOfCities);

        cityButtons = cities.stream()
                .collect(Collectors.toMap(
                        city -> city.getString("name"),
                        city -> createCityButton(city.getString("name"), questionType, category)
                ));

        cityButtons.values().forEach(button -> {
            button.getStyleClass().add("city-button");
            layout.getChildren().add(button);
        });

        root.getChildren().add(layout);
    }

    private Button createCityButton(String cityName, QuestionType questionType, String category) {
        Button cityButton = new Button(cityName);
        cityButton.setOnAction(event -> handleCityButtonClick(cityName, questionType, category, cities));
        return cityButton;
    }

    private void handleCityButtonClick(String cityName, QuestionType questionType, String category, List<Document> cities) {
        String correctCity = findCorrectCity(questionType, category, cities);
        Button correctButton = cityButtons.get(correctCity);

        if (correctButton != null) {
            correctButton.getStyleClass().add("correct-answer");
        }

        if (!cityName.equals(correctCity)) {
            Button clickedButton = cityButtons.get(cityName);
            clickedButton.getStyleClass().add("wrong-answer");
            gl.removeLife();
        }

        cityButtons.values().forEach(button -> button.setDisable(true));

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                if (gl.getHeartCount() > 0) {
                    Platform.runLater(QuestionPicker::displayRandomQuestion);
                } else {
                    Platform.runLater(gl::end);  // End the game if no lives are left
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String findCorrectCity(QuestionType questionType, String category, List<Document> cities) {
        Map<String, Integer> cityValues = cities.stream()
                .collect(Collectors.toMap(
                        city -> city.getString("name"),
                        city -> city.getInteger(category)
                ));

        return cityValues.entrySet().stream()
                .sorted((e1, e2) -> questionType == QuestionType.MOST || questionType == QuestionType.MORE ?
                        Integer.compare(e2.getValue(), e1.getValue()) : Integer.compare(e1.getValue(), e2.getValue()))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private String getLivesAsHearts(int heartCount) {
        String hearts = "";
        while(heartCount > 0){
            heartCount--;
            hearts+="❤";
        }
        return hearts;
    }
}