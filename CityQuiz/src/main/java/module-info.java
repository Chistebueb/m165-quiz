module com.example.cityquiz {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    //requires mongodb.driver;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;


    opens com.example.cityquiz to javafx.fxml;
    exports com.example.cityquiz;
}