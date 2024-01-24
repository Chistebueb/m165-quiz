package org.example;

public class App {

    private static DBConnector db;

    private static GameLoop gl = null;

    public static void main(String[] args) {
        gl = new GameLoop();
        db = new DBConnector();
        JavaFX.go();
    }

    public static GameLoop getGL() {
        return gl;
    }

    public static DBConnector getDb() {
        return db;
    }
}
