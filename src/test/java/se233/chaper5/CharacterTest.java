package se233.chaper5;

import javafx.embed.swing.JFXPanel;
import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;
import se233.chapter5.controller.DrawingLoop;
import se233.chapter5.controller.GameLoop;
import se233.chapter5.model.Character;
import se233.chapter5.view.Platform;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CharacterTest {
    private Character floatingCharacter, floatingCharacter2;
    private ArrayList<Character> characterListUnderTest;
    private Platform platformUnderTest;
    private GameLoop gameLoopUnderTest;
    private DrawingLoop drawingLoopUnderTest;
    private Method updateMethod, redrawMethod;

    @Before
    public void setup() {
        JFXPanel jfxPanel = new JFXPanel();
        floatingCharacter = new Character(30, 30, 0, 0, KeyCode.A, KeyCode.D, KeyCode.W);
        floatingCharacter2 = new Character(Platform.WIDTH - 60, 30, 0, 96, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP);
        characterListUnderTest = new ArrayList<Character>();
        characterListUnderTest.add(floatingCharacter);
        characterListUnderTest.add(floatingCharacter2);
        platformUnderTest = new Platform();
        gameLoopUnderTest = new GameLoop(platformUnderTest);
        drawingLoopUnderTest = new DrawingLoop(platformUnderTest);
        try {
            updateMethod = GameLoop.class.getDeclaredMethod("update", ArrayList.class);
            redrawMethod = DrawingLoop.class.getDeclaredMethod("paint", ArrayList.class);
            updateMethod.setAccessible(true);
            redrawMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            updateMethod = null;
            redrawMethod = null;
        }
    }

    @Test
    public void characterInitialValuesShouldMatchConstructorArguments() {
        assertEquals("Initial x", 30, floatingCharacter.getX(), 0);
        assertEquals("Initial y", 30, floatingCharacter.getY(), 0);
        assertEquals("Offset x", 0, floatingCharacter.getOffsetX(), 0.0);
        assertEquals("Offset y", 0, floatingCharacter.getOffsetY(), 0.0);
        assertEquals("Left key", KeyCode.A, floatingCharacter.getLeftKey());
        assertEquals("Right key", KeyCode.D, floatingCharacter.getRightKey());
        assertEquals("Up key", KeyCode.W, floatingCharacter.getUpKey());
    }

//    @Test
//    public void character2InitialValuesShouldMatchConstructorArguments() {
//        assertEquals("Initial x", Platform.WIDTH - 60, floatingCharacter2.getX(), 0);
//        assertEquals("Initial y", 30, floatingCharacter2.getY(), 0);
//        assertEquals("Offset x", 0, floatingCharacter2.getOffsetX(), 0.0);
//        assertEquals("Offset y", 96, floatingCharacter2.getOffsetY(), 0.0);
//        assertEquals("Left key", KeyCode.LEFT, floatingCharacter2.getLeftKey());
//        assertEquals("Right key", KeyCode.RIGHT, floatingCharacter2.getRightKey());
//        assertEquals("Up key", KeyCode.UP, floatingCharacter2.getUpKey());
//    }

    @Test
    public void characterShouldMoveToTheLeftAfterTheLeftKeyIsPressed() throws
            IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Character characterUnderTest = characterListUnderTest.get(0);
        int startX = characterUnderTest.getX();
        platformUnderTest.getKeys().add(KeyCode.A);
        updateMethod.invoke(gameLoopUnderTest, characterListUnderTest);
        redrawMethod.invoke(drawingLoopUnderTest, characterListUnderTest);
        Field isMoveLeft = characterUnderTest.getClass().getDeclaredField("isMoveLeft");
        isMoveLeft.setAccessible(true);
        assertTrue("Controller: Left key pressing is acknowledged", platformUnderTest.getKeys().isPressed(KeyCode.A));
        assertTrue("Model: Character moving left state is set", isMoveLeft.getBoolean(characterUnderTest));
        assertTrue("View: Character is moving left", characterUnderTest.getX() < startX);
    }

    // Exercise 1
    @Test
    public void characterShouldMoveToTheRightAfterTheRightKeyIsPressed() throws
            IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Character characterUnderTest = characterListUnderTest.get(0);
        int startX = characterUnderTest.getX();
        platformUnderTest.getKeys().add(KeyCode.D);
        updateMethod.invoke(gameLoopUnderTest, characterListUnderTest);
        redrawMethod.invoke(drawingLoopUnderTest, characterListUnderTest);
        Field isMoveRight = characterUnderTest.getClass().getDeclaredField("isMoveRight");
        isMoveRight.setAccessible(true);
        assertTrue("Controller: Right key pressing is acknowledged", platformUnderTest.getKeys().isPressed(KeyCode.D));
        assertTrue("Model: Character moving right state is set", isMoveRight.getBoolean(characterUnderTest));
        assertTrue("View: Character is moving right", characterUnderTest.getX() > startX);
    }

    // Exercise 2
    @Test
    public void characterCanBeJumpIfCharacterIsBeingOnTheGroundAndUpperKeyIsPressed() throws
            IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Character characterUnderTest = characterListUnderTest.get(0);
//        int startY = characterUnderTest.getY();
        platformUnderTest.getKeys().add(KeyCode.W);
        updateMethod.invoke(gameLoopUnderTest, characterListUnderTest);
        redrawMethod.invoke(drawingLoopUnderTest, characterListUnderTest);
        Field isCanJump = characterUnderTest.getClass().getDeclaredField("canJump");
        isCanJump.setAccessible(true);
        floatingCharacter.onFloor();
        assertTrue("Controller: Upper key pressing is acknowledged", platformUnderTest.getKeys().isPressed(KeyCode.W));
        assertTrue("Model: Character can jump state is set", characterUnderTest.isCanJump());
        assertEquals("View: Character is being on the ground", characterUnderTest.getY(), Platform.GROUND);
    }

    // Exercise 3
    @Test
    public void characterCannotBeJumpIfCharacterIsNotBeingOnTheGroundAndUpperKeyCannotPressed() throws
            IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Character characterUnderTest = characterListUnderTest.get(0);
        platformUnderTest.getKeys().add(KeyCode.W);
        updateMethod.invoke(gameLoopUnderTest, characterListUnderTest);
        redrawMethod.invoke(drawingLoopUnderTest, characterListUnderTest);
        Field isCanJump = characterUnderTest.getClass().getDeclaredField("canJump");
        isCanJump.setAccessible(false);
        assertTrue("Controller: Upper key pressing is acknowledged", platformUnderTest.getKeys().isPressed(KeyCode.W));
        assertFalse("Model: Character cannot jump state is set", characterUnderTest.isCanJump());
        assertTrue("View: Character is not being on the ground", characterUnderTest.getY() < Platform.GROUND);
    }

    // Exercise 4
    @Test
    public void characterCannotPassedIfCharacterIsReachingOnTheGameWall() throws
            IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Character characterUnderTest = characterListUnderTest.get(0);
        platformUnderTest.getKeys().add(KeyCode.A);
        platformUnderTest.getKeys().add(KeyCode.D);
        updateMethod.invoke(gameLoopUnderTest, characterListUnderTest);
        redrawMethod.invoke(drawingLoopUnderTest, characterListUnderTest);
        assertTrue("Controller: Left key pressing is acknowledged", platformUnderTest.getKeys().isPressed(KeyCode.A));
        assertTrue("Controller: Right key pressing is acknowledged", platformUnderTest.getKeys().isPressed(KeyCode.D));
        assertFalse("Model: Character cannot pass state is set", characterUnderTest.canPassed());
        assertTrue("View: Character is reaching the game wall", characterUnderTest.checkReachGameWallBool());
    }

    // Exercise 5
    // cr. https://stackoverflow.com/questions/15013913/checking-collision-of-shapes-with-javafx
    @Test
    public void charactersCanCollidedAndThenKnockBack() throws
            IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Character characterUnderTest1 = characterListUnderTest.get(0);
        Character characterUnderTest2 = characterListUnderTest.get(1);
        int startX = characterUnderTest1.getX();
        characterUnderTest2.setX(startX);
        int collisionVal = characterUnderTest1.getCollisionVal();
//        System.out.println(characterUnderTest1.getX());
//        System.out.println(characterUnderTest2.getX());
        platformUnderTest.getKeys().add(KeyCode.W);
        updateMethod.invoke(gameLoopUnderTest, characterListUnderTest);
        redrawMethod.invoke(drawingLoopUnderTest, characterListUnderTest);
        characterUnderTest1.collidedTest(characterUnderTest2);
        assertEquals("Model: Character knock back a bit", (characterUnderTest1.getX() - characterUnderTest2.getX()), collisionVal);
        assertTrue("View: Characters collide with each other", characterUnderTest1.getBoundsInParent().intersects(characterUnderTest2.getBoundsInParent()));
    }

    // Exercise 6
    @Test
    public void characterStompedToEachOtherThenGetScoreAndRespawn() throws
            IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Character characterUnderTest1 = characterListUnderTest.get(0);
        Character characterUnderTest2 = characterListUnderTest.get(1);
        updateMethod.invoke(gameLoopUnderTest,characterListUnderTest);
        redrawMethod.invoke(drawingLoopUnderTest,characterListUnderTest);
        int startX = characterUnderTest1.getX();
        int startY = characterUnderTest1.getY();
        characterUnderTest2.setX(startX);
        characterUnderTest2.setY(startY + Character.CHARACTER_HEIGHT);
//        System.out.println(characterUnderTest1.getY());
//        System.out.println(characterUnderTest2.getY());
        assertTrue("Model: Character Stomped To each other, collapsed, and respawn" , characterUnderTest1.isStomped(characterUnderTest2));
        assertEquals("View: Characters1 respawn X", 30, characterUnderTest1.getX(), 0);
        assertEquals("View: Characters1 respawn Y", 30+1, characterUnderTest1.getY(), 0);
        assertEquals("View: Characters2 respawn X", Platform.WIDTH-60, characterUnderTest2.getX(), 0);
        assertEquals("View: Characters2 respawn Y", 30, characterUnderTest2.getY(), 0);
    }
}
