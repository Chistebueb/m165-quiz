package org.example;


import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitDB {
    private static ConnectionString connectionString = new ConnectionString("mongodb://mongo:secretmongo@localhost:27017");
    public static void init(){
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase db = mongoClient.getDatabase("cityDB");

        db.getCollection("cities").drop();
        db.createCollection("cities");
        MongoCollection<Document> collection = db.getCollection("cities");

        List<Document> cities = new ArrayList<>();

        cities.add(new Document("name", "London")
                .append("population", 8866180)
                .append("populationDensity", 5597)
                .append("area", 1572)
                .append("avgAge", 36)
                .append("gdpPpp", 57157)
        );

        cities.add(new Document("name", "Zürich")
                .append("population", 447521)
                .append("populationDensity", 4655)
                .append("area", 87)
                .append("avgAge", 41)
                .append("gdpPpp", 68761)
        );

        cities.add(new Document("name", "Berlin")
                .append("population", 3770800)
                .append("populationDensity", 4200)
                .append("area", 891)
                .append("avgAge", 42)
                .append("gdpPpp", 40000)
        );

        cities.add(new Document("name", "Oslo")
                .append("population", 709037)
                .append("populationDensity", 1561)
                .append("area", 454)
                .append("avgAge", 37)
                .append("gdpPpp", 69167)
        );

        cities.add(new Document("name", "Warschau")
                .append("population", 1861975)
                .append("populationDensity", 3469)
                .append("area", 517)
                .append("avgAge", 40)
                .append("gdpPpp", 48681)
        );

        cities.add(new Document("name", "Moskau")
                .append("population", 12455682)
                .append("populationDensity", 4583)
                .append("area", 2510)
                .append("avgAge", 38)
                .append("gdpPpp", 45803)
        );

        cities.add(new Document("name", "Tokyo")
                .append("population", 9640742)
                .append("populationDensity", 15351)
                .append("area", 628)
                .append("avgAge", 49)
                .append("gdpPpp", 43664)
        );

        cities.add(new Document("name", "Denver")
                .append("population", 682545)
                .append("populationDensity", 1801)
                .append("area", 400)
                .append("avgAge", 36)
                .append("gdpPpp", 61795)
        );

        cities.add(new Document("name", "New York")
                .append("population", 8550405)
                .append("populationDensity", 10356)
                .append("area", 1214)
                .append("avgAge", 37)
                .append("gdpPpp", 69915)
        );

        cities.add(new Document("name", "Miami")
                .append("population", 441003)
                .append("populationDensity", 4322)
                .append("area", 143)
                .append("avgAge", 40)
                .append("gdpPpp", 44480)
        );


        collection.insertMany(cities);
        mongoClient.close();
    }

    public static List<Document> getRandomCities(int num) {
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase db = mongoClient.getDatabase("cityDB");
        MongoCollection<Document> collection = db.getCollection("cities");

        List<Document> cities = new ArrayList<>();

        collection.aggregate(
                Arrays.asList(Aggregates.sample(num))
        ).into(cities);

        return cities;
    }
}
