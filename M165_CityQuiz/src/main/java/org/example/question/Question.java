package org.example.question;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.bson.Document;
import org.example.InitDB;
import org.example.JavaFX;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Question {

    private StackPane root;
    private Map<String, Button> cityButtons;

    public Question(String question, QuestionType questionType, String category) {
        root = JavaFX.root;
        root.getChildren().clear();
        root.getStyleClass().add("normal-border");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Text title = new Text(question);
        title.getStyleClass().add("question-title");
        layout.getChildren().add(title);

        int numberOfCities = (questionType == QuestionType.MORE || questionType == QuestionType.LESS) ? 2 : 4;
        List<Document> cities = InitDB.getRandomCities(numberOfCities);

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
        cityButton.setOnAction(event -> handleCityButtonClick(cityName, questionType, category));
        return cityButton;
    }

    private void handleCityButtonClick(String cityName, QuestionType questionType, String category) {
        String correctCity = findCorrectCity(questionType, category);
        Button correctButton = cityButtons.get(correctCity);

        if (correctButton == null) {
            System.out.println("Error: Correct city not found in the buttons map.");
            return;
        }

        correctButton.getStyleClass().add("correct-answer");

        if (!cityName.equals(correctCity)) {
            Button clickedButton = cityButtons.get(cityName);
            if (clickedButton != null) {
                clickedButton.getStyleClass().add("wrong-answer");
            }
            root.getStyleClass().add("wrong-border");
        }

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Platform.runLater(QuestionPicker::displayRandomQuestion);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String findCorrectCity(QuestionType questionType, String category) {
        int numberOfCities = (questionType == QuestionType.MORE || questionType == QuestionType.LESS) ? 2 : 4;
        List<Document> cities = InitDB.getRandomCities(numberOfCities);

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
}