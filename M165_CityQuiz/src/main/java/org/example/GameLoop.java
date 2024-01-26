package org.example;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.bson.Document;

import java.util.List;

public class GameLoop {
    private int heartCount;
    private String username = "";
    private int score = 0;

    //Timer:
    private final Timeline timeline;
    private double startTime;
    private Duration timeElapsed;

    public GameLoop(){
        heartCount = 3;
        timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void startTime() {
        startTime = System.currentTimeMillis();
        timeline.play();
    }
    private void update(){

    }

    public int getHeartCount(){
        return heartCount;
    }

    public void removeLife(){
        heartCount--;
    }
    public void end(){
        System.out.println("Game Over");
        timeline.stop();
        double stopTime = System.currentTimeMillis();
        timeElapsed = Duration.millis(stopTime - startTime);
        System.out.println("Player: " + username);
        System.out.println("Score: " + score);
        System.out.println("Time: " + this.getElapsedTime().toMillis()/1000 + "s");
        showResults();
    }

    private void showResults(){

        DBConnector dbConnector = App.getDb();

        dbConnector.insertUser(username, score, getElapsedTime());
        List<Document> topPlayers = dbConnector.getTopPlayers();

        long rank = dbConnector.getRank(username, score, getElapsedTime());

        // Assuming you have a JavaFX TableView set up to display the results
        TableView<Player> tableView = new TableView<>();
        TableColumn<Player, String> rankColumn = new TableColumn<>("Rank");
        TableColumn<Player, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Player, Number> scoreColumn = new TableColumn<>("Score");
        TableColumn<Player, Number> timeColumn = new TableColumn<>("Time");


        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        //get rank
        rankColumn.setCellFactory(col -> new TableCell<Player, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    // This gets the index of the current row and adds 1 to start ranking at 1 instead of 0
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });

        tableView.getColumns().add(rankColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(scoreColumn);
        tableView.getColumns().add(timeColumn);

        rankColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        nameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        scoreColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        timeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));


        // Convert documents to your Player model and add to the table
        topPlayers.forEach(doc -> tableView.getItems().add(new Player(
                doc.getString("name"),
                doc.getInteger("score"),
                (Double) doc.get("time")
        )));


        //no scrolling >:(
        tableView.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Text title = new Text("Result");
        title.getStyleClass().add("big-title");
        layout.getChildren().add(title);

        Text scoreText = new Text("Your score: " + score);
        scoreText.getStyleClass().add("result");
        layout.getChildren().add(scoreText);

        Text timeText = new Text("Your time: " + getElapsedTime().toMillis() / 1000 + "s");
        timeText.getStyleClass().add("result");
        layout.getChildren().add(timeText);

        Text rankText = new Text("Your rank: " + rank + ".");
        rankText.getStyleClass().add("result");
        layout.getChildren().add(rankText);

        Button restartButton = new Button("Play Again");
        restartButton.setOnAction(event -> restart());
        restartButton.getStyleClass().add("city-button");
        layout.getChildren().add(restartButton);

        tableView.setMaxHeight(240);

        tableView.getStyleClass().add("table-view");
        layout.getChildren().add(tableView);


        StackPane root = JavaFX.root;
        root.getChildren().clear();
        root.getChildren().add(layout);
    }

    public void restart(){
        App.setGl(new GameLoop());
        JavaFX.restart();
    }

    public Duration getElapsedTime() {
        return timeElapsed;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void correctAnswer(){
        score++;
    }
}
