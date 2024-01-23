package com.example.cityquiz;


import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import com.mongodb.ConnectionString;
import com.mongodb.client.model.Aggregates;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitDB {
    private static String connectionString = "mongodb://mongo:secretmongo@localhost:27017/cityDB";
    static MongoClient mongoClient = MongoClients.create(connectionString);
    public static void init(){
        MongoDatabase db = mongoClient.getDatabase("cityDB");

        Bson command = new BsonDocument("ping", new BsonInt64(1));
        Document commandResult = db.runCommand(command);
        System.out.println("Pinged your deployment. You successfully connected to MongoDB!");

        MongoCollection<Document> collection = db.getCollection("cities");

        List<Document> cities = new ArrayList<>();
        cities.add(new Document("name", "Tokio")
                .append("population", 1)
                .append("populationDensity", 1)
                .append("area", 1)
                .append("average rent", 1)
                .append("populationDensity", 1)
        );
        cities.add(new Document("name", "Zuerich")
                .append("population", 1)
                .append("populationDensity", 1)
                .append("area", 1)
                .append("average rent", 1)
                .append("populationDensity", 1)
        );
        cities.add(new Document("name", "Zuerich")
                .append("population", 1)
                .append("populationDensity", 1)
                .append("area", 1)
                .append("average rent", 1)
                .append("populationDensity", 1)
        );
        cities.add(new Document("name", "Zuerich")
                .append("population", 1)
                .append("populationDensity", 1)
                .append("area", 1)
                .append("average rent", 1)
                .append("populationDensity", 1)
        );
        //...

        collection.insertMany(cities);
        mongoClient.close();
    }

    public static List<Document> getRandomCities() {
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase db = mongoClient.getDatabase("cityDB");
        MongoCollection<Document> collection = db.getCollection("cities");

        List<Document> cities = new ArrayList<>();

        collection.aggregate(
                Arrays.asList(Aggregates.sample(4))
        ).into(cities);

        return cities;
    }
}
