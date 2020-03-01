package org.nullpointerid.spaceago.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import org.nullpointerid.spaceago.SpaceShooter;
import org.nullpointerid.spaceago.tools.MovingBackground;

public class MainMenuScreen implements Screen {

    private SpaceShooter game;
    MainGameScreen main;

    private static final int EXIT_BUTTON_WIDTH = 150;
    private static final int EXIT_BUTTON_Height = 50;
    private static final int PLAY_BUTTON_WIDTH = 150;
    private static final int PLAY_BUTTON_Height = 50;

    private static final int EXIT_BUTTON_Y = 250;
    private static final int PLAY_BUTTON_Y = 325;

    private Texture background;
    private Texture playActive;
    private Texture playInActive;
    private Texture exitActive;
    private Texture exitInActive;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;

    public MainMenuScreen(SpaceShooter game) {
        this.game = game;

        Gdx.input.setCursorCatched(false);

        background = new Texture("images/menuBackg.jfif");
        playActive = new Texture("images/play_button_active.png");
        playInActive = new Texture("images/play_button_inactive.png");
        exitActive = new Texture("images/exit_button_active.png");
        exitInActive = new Texture("images/exit_button_inactive.png");
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Halo3.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 55;
        fontParameter.borderWidth = 10;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.LIGHT_GRAY;
        font = fontGenerator.generateFont(fontParameter);

        game.movingBackground.setFixedSpeed(true);
        game.movingBackground.setSpeed(MovingBackground.DEFAULT_SPEED);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.movingBackground.updateRender(delta, game.batch);

        int xExit = SpaceShooter.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2; //X starting point
        int xPlay = SpaceShooter.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;

        //Menu icon
        font.draw(game.batch, "SpaceAgo", 0, SpaceShooter.HEIGHT - 50);

        //Start button
        if (Gdx.input.getX() < xPlay + PLAY_BUTTON_WIDTH && Gdx.input.getX() > xPlay && SpaceShooter.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_Height && SpaceShooter.HEIGHT - Gdx.input.getY() > PLAY_BUTTON_Y) {
            game.batch.draw(playActive, SpaceShooter.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_Height);
            if (Gdx.input.isTouched()) {
                this.dispose();
                game.setScreen(new MainGameScreen(game));
            }
        } else {
            game.batch.draw(playInActive, SpaceShooter.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_Height);
        }

        //Exit button
        if (Gdx.input.getX() < xExit + EXIT_BUTTON_WIDTH && Gdx.input.getX() > xExit && SpaceShooter.HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_Height && SpaceShooter.HEIGHT - Gdx.input.getY() > EXIT_BUTTON_Y) {
            game.batch.draw(exitActive, SpaceShooter.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_Height);
            if (Gdx.input.isTouched()) {
                Gdx.app.exit();
            }
        } else {
            game.batch.draw(exitInActive, SpaceShooter.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_Height);
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
