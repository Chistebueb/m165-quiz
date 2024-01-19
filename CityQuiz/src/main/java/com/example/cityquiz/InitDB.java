package com.example.cityquiz;


import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import com.mongodb.ConnectionString;
import org.bson.Document;

import java.util.List;

public class InitDB {
    public void init(){
        String connectionString = System.getenv("mongodb://mongo:secretmongo@localhost:27017/cityDB");
        if (connectionString == null || connectionString.isEmpty()) {
            System.out.println("No connection string provided.");
            return;
        }

        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase db = mongoClient.getDatabase("cityDB");


        db.createCollection("cities");
        MongoCollection<Document> collection = db.getCollection("cities");

        List cities = null;
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

        collection.insertMany(cities);
    }
}
