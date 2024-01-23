package com.example.cityquiz;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {
    public Question(String question, QuestionType questionType) {
        // Get the JavaFX root (StackPane) - assuming you have a way to access it
        StackPane root = JavaFX.root;
        root.getChildren().clear();

        // Add the question as a title
        Text title = new Text(question);
        root.getChildren().add(title);

        // Database operations to get 4 random cities
        List<Document> cities = InitDB.getRandomCities();

        // Create and add buttons for each city
        for (Document city : cities) {
            String cityName = city.getString("name");
            Button cityButton = new Button(cityName);
            root.getChildren().add(cityButton);
        }

        // Adjust layout and styles as needed to fit the screen size
        // This part is heavily dependent on your specific application layout
    }
}
