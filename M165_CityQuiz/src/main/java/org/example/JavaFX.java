package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.question.QuestionPicker;

public class JavaFX extends Application {

    public static StackPane root;

    public static void go(){
        launch();
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cities Quiz");

        root = new StackPane();
        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        QuestionPicker.displayRandomQuestion();
        primaryStage.show();
    }
}
