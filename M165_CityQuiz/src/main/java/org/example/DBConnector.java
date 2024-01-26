package org.example;


import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import javafx.util.Duration;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBConnector {
    private final ConnectionString connectionString = new ConnectionString("mongodb://mongo:secretmongo@localhost:27017");
    private MongoDatabase db;
    public DBConnector(){
        try {
            MongoClient mongoClient;
            mongoClient = MongoClients.create(connectionString);
            db = mongoClient.getDatabase("cityDB");
            System.out.println("succesfully connected to cityDB");
        }catch(RuntimeException e) {
            throw e;
        }
    }

    public List<Document> getRandomCities(int num) {
        try{
        MongoCollection<Document> cities = db.getCollection("cities");
        List<Document> citiesList = new ArrayList<>();

        cities.aggregate(
                Arrays.asList(Aggregates.sample(num))
        ).into(citiesList);
            return citiesList;
        }catch(MongoException me){
            System.err.println("An error occurred while getting cities" + me);
            throw me;
        }
    }

    public List<Document> getTopPlayers(String username, int score, Duration elapsedTime) {
        MongoCollection<Document> collection = db.getCollection("user");

        Document currentUser = new Document("name", username)
                .append("score", score)
                .append("time", elapsedTime.toMillis() / 1000);

        collection.insertOne(currentUser);


        List<Bson> aggregationPipeline = Arrays.asList(
                Aggregates.sort(Sorts.orderBy(Sorts.descending("score"), Sorts.ascending("time"))),
                Aggregates.limit(9)
        );

        // Execute aggregation
        List<Document> topPlayers = collection.aggregate(aggregationPipeline).into(new ArrayList<>());
        return topPlayers;
    }
}
