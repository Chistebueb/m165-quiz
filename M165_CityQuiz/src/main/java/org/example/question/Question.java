package org.example.question;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.bson.Document;
import org.example.App;
import org.example.GameLoop;
import org.example.JavaFX;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Question {

    private final StackPane root;
    private final Map<String, Button> cityButtons;
    private final List<Document> cities;
    private final GameLoop gl;
    private final Text livesDisplay;
    private final Text animText;

    public Question(String question, QuestionType questionType, String category) {
        gl = App.getGL();
        root = JavaFX.root;
        root.getChildren().clear();
        root.getStyleClass().add("border");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        livesDisplay = new Text(getLivesAsHearts(gl.getHeartCount()));
        livesDisplay.getStyleClass().add("lives-display");
        layout.getChildren().add(livesDisplay);

        StackPane heartTextContainer = new StackPane();
        heartTextContainer.getChildren().add(livesDisplay);


        animText = new Text("❤");
        animText.getStyleClass().add("invisible");

        heartTextContainer.getChildren().add(animText);

        StackPane.setMargin(animText, new Insets(0, 0, 0, 55 * (gl.getHeartCount() -1)));

        layout.getChildren().add(heartTextContainer);

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

    private int loadTime = 400;
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
            livesDisplay.setText(getLivesAsHearts(gl.getHeartCount()));
            loadTime += 400;
            playHeartAnim();
        }
        else{
            gl.correctAnswer();
        }

        cityButtons.values().forEach(button -> button.setDisable(true));

        new Thread(() -> {
            try {
                Thread.sleep(loadTime);
                if (gl.getHeartCount() > 0) {
                    Platform.runLater(QuestionPicker::displayRandomQuestion);
                } else {
                    Platform.runLater(gl::end);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String findCorrectCity(QuestionType questionType, String category, List<Document> cities) {
        Map<String, Double> cityValues = cities.stream()
                .collect(Collectors.toMap(
                        city -> city.getString("name"),
                        city -> city.getDouble(category)
                ));

        return cityValues.entrySet().stream().min((e1, e2) -> questionType == QuestionType.MOST || questionType == QuestionType.MORE ?
                        Double.compare(e2.getValue(), e1.getValue()) : Double.compare(e1.getValue(), e2.getValue()))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private String getLivesAsHearts(int heartCount) {
        StringBuilder hearts = new StringBuilder();
        while(heartCount > 0){
            heartCount--;
            hearts.append("❤");
        }
        return hearts.toString();
    }

    private void playHeartAnim() {

        animText.setTranslateY(0);
        animText.setTranslateX(0); // Reset horizontal position
        animText.setRotate(0);
        animText.setOpacity(1.0);

        TranslateTransition bounceUp = new TranslateTransition(Duration.millis(250), animText);
        bounceUp.setByY(-25); // Move up
        bounceUp.setInterpolator(Interpolator.SPLINE(0.1, 0.1, 0.2, 1));

        // Downward fall transition with a custom interpolator for a smooth start and a more natural acceleration
        TranslateTransition fallDown = new TranslateTransition(Duration.millis(550), animText);
        fallDown.setByY(60); // Move down
        fallDown.setInterpolator(Interpolator.SPLINE(0.1, 0.0, 0.3, 0));

        // Rightward movement transition
        TranslateTransition moveRight = new TranslateTransition(Duration.millis(800), animText);
        moveRight.setByX(25); // Move slightly to the right

        // Rotate transition
        RotateTransition rotate = new RotateTransition(Duration.millis(800), animText);
        rotate.setByAngle(20); // Slight rotation

        // Fade transition
        FadeTransition fade = new FadeTransition(Duration.millis(800), animText);
        fade.setToValue(0); // Fade out

        // Sequential transition for the bounce and fall
        SequentialTransition bounceAndFall = new SequentialTransition(bounceUp, fallDown);

        // Parallel transition to combine all effects including the rightward movement
        ParallelTransition allTransitions = new ParallelTransition(bounceAndFall, rotate, fade, moveRight);

        // Play the combined animation
        PauseTransition pauseBeforeStarting = new PauseTransition(Duration.millis(10)); // Small pause
        pauseBeforeStarting.setOnFinished(event -> allTransitions.play());
        pauseBeforeStarting.play();

        animText.getStyleClass().remove("invisible");
        animText.getStyleClass().add("lives-gone-display");
    }

}