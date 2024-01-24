package org.example;

public class GameLoop {
    private int heartCount;

    public GameLoop(){
        heartCount = 3;
    }

    public int getHeartCount(){
        return heartCount;
    }

    public void removeLife(){
        heartCount--;
    }
    public void end(){
        System.out.println("Game Over");
    }
}
