package se233.chapter5.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.chapter5.Launcher;
import se233.chapter5.model.Character;
import se233.chapter5.model.Keys;

import java.util.ArrayList;

public class Platform extends Pane {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public final static int GROUND = 300;
    private Image platformImg;
    private ArrayList<Character> characterList ;
    private ArrayList<Score> scoreList;

    private Keys keys;

    public Platform() {
        characterList = new ArrayList();
        scoreList = new ArrayList();
        keys = new Keys();
        platformImg = new Image(Launcher.class.getResourceAsStream("assets/Background.png"));
        ImageView backgroundImg = new ImageView(platformImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);
        characterList.add(new Character(30, 30,0,0, KeyCode.A,KeyCode.D,KeyCode.W));
        characterList.add(new Character(Platform.WIDTH-60, 30,0,96, KeyCode.LEFT,KeyCode.RIGHT,KeyCode.UP));
        scoreList.add(new Score(30,GROUND+30));
        scoreList.add(new Score(Platform.WIDTH-60,GROUND+30));
        getChildren().add(backgroundImg);
        getChildren().addAll(characterList);
        getChildren().addAll(scoreList);
    }
    public ArrayList<Character> getCharacterList() {
        return characterList;
    }
    public Keys getKeys() {
        return keys;
    }

    public ArrayList<Score> getScoreList() {
        return  scoreList;
    }
}