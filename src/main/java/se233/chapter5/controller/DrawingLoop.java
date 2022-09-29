package se233.chapter5.controller;
import se233.chapter5.model.Character;
import se233.chapter5.view.Platform;

import java.util.ArrayList;

public class DrawingLoop implements Runnable {
    private Platform platform;
    private int frameRate;
    private float interval;
    private boolean running;
    public DrawingLoop(Platform platform) {
        this.platform = platform;
        frameRate = 30;
        interval = 1000.0f / frameRate; // 1000 ms = 1 second
        running = true;
    }
    private void checkDrawCollisions(ArrayList<Character> characterList) {
        for (Character character : characterList ) {
            character.checkReachGameWall();
            character.checkReachHighest();
            character.checkReachFloor();
        }
        for(Character cA : characterList){
            for(Character cB : characterList){
                if(cA!=cB){
                    if(cA.getBoundsInParent().intersects(cB.getBoundsInParent())){
                        cA.collided(cB);
                        cB.collided(cA);
                        return;
                    }
                }
            }
        }
    }
    private void paint(ArrayList<Character> characterList) {
        for (Character character : characterList ) {
            character.repaint();
        }
    }
    @Override
    public void run() {
        while (running) {
            float time = System.currentTimeMillis();

            checkDrawCollisions(platform.getCharacterList());
            paint(platform.getCharacterList());

            time = System.currentTimeMillis() - time;
            if (time < interval) {
                try {
                    Thread.sleep((long) (interval - time));
                } catch (InterruptedException e) {
                }
            } else {
                try {
                    Thread.sleep((long) (interval - (interval % time)));
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
