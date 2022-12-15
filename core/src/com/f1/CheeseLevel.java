package com.f1;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class CheeseLevel extends BaseScreen {

    CheeseGame game;
    ArrayList<BaseActor> bricks;
    static Characters player1 = new Characters("Player1");
    static Characters player2 = new Characters("Player2");
    private Button pauseExitButton;
    private Button pauseContinueButton;
    private BaseActor blackBackground;
    private Label pauseMainMenu;
    private float mousey1Speed;
    private float mousey2Speed;
    private BaseActor brick;
    private BaseActor winText;
    private BaseActor cheese;
    private boolean win;
    public static Music backgroundMusic;
    private boolean pausedScreen;
    maze mazeObj;
    private float timeElipsed;
    private Label timeLabel;
    final int mapWidth = 780;
    final int mapHeight = 780;

    public CheeseLevel(CheeseGame g) {
        super(g);
        this.game = g;
        bricks = new ArrayList<BaseActor>();
        mazeObj = new maze(10, 10);
        mazeObj.generateMaze();
        for (int i = 0; i < mazeObj.maze.length; i++) {
            for (int j = 0; j < mazeObj.maze[0].length; j++) {
                if (mazeObj.maze[i][j] == 'X') {
                    brick = new BaseActor();
                    brick.setTexture(new Texture(Gdx.files.internal("assets/Brick_gray.jpeg")));
                    brick.setSize(19.2f, 19.2f);
                    brick.setPosition(i * 19.2f, j * 19.2f);
                    brick.setOrigin(brick.getWidth() / 2, brick.getHeight() / 2);
                    bricks.add(brick);
                    mainStage.addActor(brick);
                }
            }
        }
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void create() {

        pausedScreen = false;

        timeElipsed = 0;

        cheese = new BaseActor();
        cheese.setTexture(new Texture(Gdx.files.internal("assets/cheese.png")));
        float randX = MathUtils.random(0, viewWidth);
        float randY = MathUtils.random(0, viewHeight);
        cheese.setPosition(randX, randY);
        cheese.setOrigin(cheese.getWidth() / 2, cheese.getHeight() / 2);

        mainStage.addActor(cheese);

        // The Players
        if(!player1.textureChanged) {
            player1.setTexture(new Texture(Gdx.files.internal("assets/redBall.png")));
        }
        if(!player2.textureChanged) {
            player2.setTexture(new Texture(Gdx.files.internal("assets/blueBall.png")));
        }
        player1.setPosition(20, 20);
        player2.setPosition(725, 20);
        player1.setSize(40, 40);
        player2.setSize(40, 40);
        mainStage.addActor(player1);
        mainStage.addActor(player2);

        // "Win" Text
        winText = new BaseActor();
        winText.setTexture(new Texture(Gdx.files.internal("assets/you-win.png")));
        winText.setPosition(170, 60);
        winText.setVisible(false);
        uiStage.addActor(winText);

        BitmapFont font = new BitmapFont();
        String text = "Time: 0";
        LabelStyle style = new LabelStyle(font, Color.WHITE);
        timeLabel = new Label(text, style);
        timeLabel.setFontScale(2);
        timeLabel.setPosition(600, 700);
        uiStage.addActor(timeLabel);

        win = false;
    }


    @Override
    public void update(float dt) {

        // The whole movement part
        Rectangle player1Rectangle = player1.getBoundingRectangle();
        Rectangle player2REctangle = player2.getBoundingRectangle();
        if (!pausedScreen) {

            mainStage.addActor(timeLabel);
            player1.velocityX = 0;
            player1.velocityY = 0;
            player2.velocityX = 0;
            player2.velocityY = 0;

            int indexX = (int) (player1.getX() / 19.02);
            int indexY = (int) (player1.getY() / 19.02);

            for (int i = 0; i < bricks.size(); i++) {
                // First Player
                if (Gdx.input.isKeyPressed(Keys.LEFT)) { // Left
                    if (player1.getY() + 65 > bricks.get(i).getY()
                            && player1.getY() + 10 < bricks.get(i).getY() + bricks.get(i).getHeight()) {
                        if (!player1Rectangle.contains(bricks.get(i).getX() + bricks.get(i).getWidth(),
                                player1.getY())) {
                            player1.velocityX -= (150);
                        }
                    } else {
                        player1.velocityX -= (150);
                    }
                }
                if (Gdx.input.isKeyPressed(Keys.RIGHT)) { // Right
                    if (player1.getY() + 65 > bricks.get(i).getY()
                            && player1.getY() + 10 < bricks.get(i).getY() + bricks.get(i).getHeight()) {
                        if (!player1Rectangle.contains(bricks.get(i).getX(), player1.getY())) {
                            player1.velocityX += (150);
                        }
                    }

                    else {
                        player1.velocityX += (150);
                    }
                }
                if (Gdx.input.isKeyPressed(Keys.UP)) { // Up
                    if (player1.getX() + 65 > bricks.get(i).getX()
                            && player1.getX() + 10 < bricks.get(i).getX() + bricks.get(i).getWidth()) {
                        if (!player1Rectangle.contains(player1.getX(), bricks.get(i).getY())) {
                            player1.velocityY += (150);
                        }
                    } else {
                        player1.velocityY += (150);
                    }
                }
                if (Gdx.input.isKeyPressed(Keys.DOWN)) { // Down
                    if (player1.getX() + 65 > bricks.get(i).getX()
                            && player1.getX() + 10 < bricks.get(i).getX() + bricks.get(i).getWidth()) {
                        if (!player1Rectangle.contains(player1.getX(),
                                bricks.get(i).getY() + bricks.get(i).getHeight())) {
                            player1.velocityY -= (150);
                        }
                    } else {
                        player1.velocityY -= (150);
                    }
                }

                // Second Player
                if (Gdx.input.isKeyPressed(Keys.A)) { // Left
                    if (player2.getY() + 65 > bricks.get(i).getY()
                            && player2.getY() + 10 < bricks.get(i).getY() + bricks.get(i).getHeight()) {
                        if (!player2REctangle.contains(bricks.get(i).getX() + bricks.get(i).getWidth(),
                                player2.getY())) {
                            player2.velocityX -= (150);
                        }
                    } else {
                        player2.velocityX -= (150);
                    }
                }
                if (Gdx.input.isKeyPressed(Keys.D)) { // Right
                    if (player2.getY() + 65 > bricks.get(i).getY()
                            && player2.getY() + 10 < bricks.get(i).getY() + bricks.get(i).getHeight()) {
                        if (!player2REctangle.contains(bricks.get(i).getX(), player2.getY())) {
                            player2.velocityX += (150);
                        }
                    } else {
                        player2.velocityX += (150);
                    }
                }
                if (Gdx.input.isKeyPressed(Keys.W)) { // Up
                    if (player2.getX() + 65 > bricks.get(i).getX()
                            && player2.getX() + 10 < bricks.get(i).getX() + bricks.get(i).getWidth()) {
                        if (!player2REctangle.contains(player2.getX(), bricks.get(i).getY())) {
                            player2.velocityY += (150);
                        }
                    } else {
                        player2.velocityY += (150);
                    }
                }
                if (Gdx.input.isKeyPressed(Keys.S)) { // Down
                    if (player2.getX() + 65 > bricks.get(i).getX()
                            && player2.getX() + 10 < bricks.get(i).getX() + bricks.get(i).getWidth()) {
                        if (!player2REctangle.contains(player2.getX(),
                                bricks.get(i).getY() + bricks.get(i).getHeight())) {
                            player2.velocityY -= (150);
                        }
                    } else {
                        player2.velocityY -= (150);
                    }
                }
            }
            

            // Escape button
            if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
                pausedScreen = true;
                Skin skin = new Skin(Gdx.files.internal("assets/Glassy_UI_Skin/glassyui/glassy-ui.json"));
                blackBackground = new BaseActor();
                blackBackground.setTexture(new Texture(Gdx.files.internal("assets/blackBackground.jpeg")));
                blackBackground.setPosition(0, 0);
                blackBackground.setSize(Gdx.graphics.getWidth() * 4, Gdx.graphics.getHeight() * 4);
                mainStage.addActor(blackBackground);

                pauseExitButton = new TextButton("Exit", skin, "small");
                pauseExitButton.setPosition(300, 350);
                pauseExitButton.setSize(200, 60);
                mainStage.addActor(pauseExitButton);

                pauseContinueButton = new TextButton("Continue", skin, "small");
                pauseContinueButton.setPosition(300, 450);
                pauseContinueButton.setSize(200, 60);
                mainStage.addActor(pauseContinueButton);

                pauseMainMenu = new Label("Press M to display the Main Menu", skin);
                pauseMainMenu.setPosition(260, 200);
                pauseMainMenu.setSize(100, 100);
                mainStage.addActor(pauseMainMenu);
            }

            player1.setX(MathUtils.clamp(player1.getX(), 0, mapWidth - player1.getWidth()));
            player1.setY(MathUtils.clamp(player1.getY(), 0, mapHeight - player1.getHeight()));

            player2.setX(MathUtils.clamp(player2.getX(), 0, mapWidth - player2.getWidth()));
            player2.setY(MathUtils.clamp(player2.getY(), 0, mapHeight - player2.getHeight()));

            Rectangle cheeseRectangle = cheese.getBoundingRectangle();
            Rectangle mousey2Rectangle = player2.getBoundingRectangle();

            // If 2 rectangle are in the same location
            if (!win && (cheeseRectangle.contains(player1Rectangle) || cheeseRectangle.contains(mousey2Rectangle))) {
                mousey1Speed += 100;
                mousey2Speed += 50;
                win = true;
                // Disappearing of the cheese
                Action spinShrinkFadeOut = Actions.parallel(
                        // Transparency volume
                        Actions.alpha(1),
                        // 360 degree
                        Actions.rotateBy(360, 1),
                        // 0 for 2 seconds
                        Actions.scaleTo(0, 0, 2),
                        Actions.fadeOut(1));
                // Add this action
                cheese.addAction(spinShrinkFadeOut);
                Action fadeInColorCycleForever = Actions.sequence(
                        Actions.alpha(0),
                        // Visible
                        Actions.show(),
                        // Duration of fadeIn
                        Actions.fadeIn(2),
                        Actions.forever(
                                Actions.sequence(
                                        // Transitions from the color at the time this action starts to the specified
                                        // color.
                                        Actions.color(new Color(1, 0, 0, 1), 1),
                                        Actions.color(new Color(0, 0, 1, 1), 1))));
                winText.addAction(fadeInColorCycleForever);
                // winText.setVisible(true);
            }
            if (!win) {
                timeElipsed += dt;
                timeLabel.setText("Time: " + (int) timeElipsed);
            }
        }
        // if pause == true
        else {
            timeLabel.remove();
            handlePause();
        }
    }

    public void handlePause() {
        if (Gdx.input.isKeyPressed(Keys.M)) {
            game.setScreen(new CheeseMenu(game));
        }
        if (pauseContinueButton.isPressed()) {
            pausedScreen = false;
            pauseExitButton.remove();
            pauseContinueButton.remove();
            pauseMainMenu.remove();
            blackBackground.remove();
        }
        if (pauseExitButton.isPressed()) {
            Gdx.app.exit();
        }
    }
}