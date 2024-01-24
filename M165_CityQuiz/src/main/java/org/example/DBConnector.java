package org.example;


import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;

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
            System.out.println(citiesList);
        return citiesList;
        }catch(MongoException me){
            System.err.println("An error occurred while getting cities" + me);
            throw me;
        }
    }
}
