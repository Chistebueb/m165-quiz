package org.example;


import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import javafx.util.Duration;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.question.QuestionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DBConnector {
    private final MongoDatabase db;
    public DBConnector(){
        MongoClient mongoClient;
        ConnectionString connectionString = new ConnectionString("mongodb://mongo:secretmongo@localhost:27017");
        mongoClient = MongoClients.create(connectionString);
        db = mongoClient.getDatabase("cityDB");
        System.out.println("succesfully connected to cityDB");
    }

    public List<Document> getRandomCities(int num) {
        try{
        MongoCollection<Document> cities = db.getCollection("cities");
        List<Document> citiesList = new ArrayList<>();

        cities.aggregate(
                Collections.singletonList(Aggregates.sample(num))
        ).into(citiesList);
            return citiesList;
        }catch(MongoException me){
            System.err.println("An error occurred while getting cities" + me);
            throw me;
        }
    }

    public void insertUser(String username, int score, Duration elapsedTime){
        MongoCollection<Document> collection = db.getCollection("user");

        Document currentUser = new Document("name", username)
                .append("score", score)
                .append("time", elapsedTime.toMillis() / 1000);

        collection.insertOne(currentUser);
    }

    public List<Document> getTopPlayers() {
        MongoCollection<Document> collection = db.getCollection("user");
        List<Bson> aggregationPipeline = Arrays.asList(
                Aggregates.sort(Sorts.orderBy(Sorts.descending("score"), Sorts.ascending("time"))),
                Aggregates.limit(5)
        );

        // Execute aggregation
        return collection.aggregate(aggregationPipeline).into(new ArrayList<>());
    }

    public int getRank(String username, int score, Duration elapsedTime){
        MongoCollection<Document> collection = db.getCollection("user");

        Document currentUser = new Document("name", username)
                .append("score", score)
                .append("time", elapsedTime.toMillis() / 1000);

        return (int) (collection.countDocuments(Filters.gt("score", currentUser.getInteger("score"))) + 1);
    }

    public String getQuestion(String category, QuestionType questionType) {
        MongoCollection<Document> questions = db.getCollection("questions");
        AggregateIterable<Document> result = questions.aggregate(Arrays.asList(
                new Document("$match", new Document("category", category)),
                new Document("$project", new Document(questionType.toString(), "$questionType." + questionType))
        ));

        Document questionDocument = result.first();
        if (questionDocument != null && questionDocument.containsKey(questionType.toString())) {
            return questionDocument.getString(questionType.toString());
        }

        return "No matching question found for the specified category and question type.";

    }
}
