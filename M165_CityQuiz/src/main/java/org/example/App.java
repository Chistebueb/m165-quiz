package org.example;

public class App {
    private static GameLoop gl = null;
    public static void main(String[] args){
        gl = new GameLoop();
        InitDB.init();
        JavaFX.go();
    }

    public static GameLoop getGL(){
        return gl;
    }
}
