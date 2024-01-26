package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.question.QuestionPicker;

import java.util.Objects;

public class JavaFX extends Application {

    public static StackPane root;

    public static void go(){
        launch();
    }

    //needed for restart
    private static Stage primaryStageAttribute;
    @Override
    public void start(Stage primaryStage) {
        primaryStageAttribute = primaryStage;
        primaryStage.setTitle("Cities Quiz");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Text title = new Text("Enter Username:");
        title.getStyleClass().add("question-title");

        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Username");
        usernameInput.getStyleClass().add("input-field");

        Button startBtn = new Button("Start");
        startBtn.setOnAction(event -> handleStartButtonClick(usernameInput.getText()));
        startBtn.getStyleClass().add("city-button");

        layout.getChildren().addAll(title, usernameInput, startBtn);
        root = new StackPane();
        root.getChildren().add(layout);


        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(Objects.requireNonNull(JavaFX.class.getResource("style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void restart(){
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Text title = new Text("Enter Username:");
        title.getStyleClass().add("question-title");

        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Username");
        usernameInput.getStyleClass().add("input-field");

        Button startBtn = new Button("Start");
        startBtn.setOnAction(event -> handleStartButtonClick(usernameInput.getText()));
        startBtn.getStyleClass().add("city-button");

        layout.getChildren().addAll(title, usernameInput, startBtn);
        root = new StackPane();
        root.getChildren().add(layout);


        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(Objects.requireNonNull(JavaFX.class.getResource("style.css")).toExternalForm());
        primaryStageAttribute.setScene(scene);
        primaryStageAttribute.show();
    }

    private static void handleStartButtonClick(String username){
        if (!username.equals("") && !username.equals(" ")) {
            App.getGL().setUsername(username);
            App.getGL().startTime();
            QuestionPicker.displayRandomQuestion();
        }
    }

}
