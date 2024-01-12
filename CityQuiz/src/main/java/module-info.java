module com.example.cityquiz {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.cityquiz to javafx.fxml;
    exports com.example.cityquiz;
}