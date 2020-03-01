package org.nullpointerid.spaceago.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import org.nullpointerid.spaceago.SpaceShooter;
import org.nullpointerid.spaceago.tools.MovingBackground;

public class GameOverScreen implements Screen {
    private SpaceShooter game;
    private static final int BANNER_WIDTH = 350;
    private static final int BANNER_HEIGHT = 100;

    private int score, highscore;

    private Texture gameOverBanner;
    private BitmapFont scoreFont;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator fontGenerator2;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter2;
    private BitmapFont font;
    private BitmapFont font2;

    public GameOverScreen(SpaceShooter game, int score) {
        this.game = game;
        this.score = score;
        Gdx.input.setCursorCatched(false);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Halo3.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 55;
        fontParameter.borderWidth = 10;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.WHITE;
        font = fontGenerator.generateFont(fontParameter);

        fontGenerator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/game_over.ttf"));
        //fontGenerator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/hunger_games.ttf")); // hea game over
        fontParameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter2.size = 70;
        fontParameter2.borderWidth = 10;
        fontParameter2.borderColor = Color.BLACK;
        fontParameter2.color = Color.WHITE;
        font2 = fontGenerator2.generateFont(fontParameter2);

        //Get and save score
        Preferences prefs = Gdx.app.getPreferences("spaceshooter");
        this.highscore = prefs.getInteger("highscore", 0);

        //Check if score beats highscore
        if (score > highscore) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }

        //Textures and fonts
        gameOverBanner = new Texture("images/game_over.png");
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));

        game.movingBackground.setFixedSpeed(true);
        game.movingBackground.setSpeed(MovingBackground.DEFAULT_SPEED);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.movingBackground.updateRender(delta, game.batch);
        /*
        game.batch.draw(gameOverBanner, SpaceShooter.WIDTH / 2 - BANNER_WIDTH / 2, SpaceShooter.HEIGHT - BANNER_HEIGHT - 15, BANNER_WIDTH, BANNER_HEIGHT);
        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score: \n" + score, Color.WHITE, 0, Align.left, false);
        GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "Highscore: \n" + highscore, Color.WHITE, 0, Align.left, false);
        scoreFont.draw(game.batch, scoreLayout, SpaceShooter.WIDTH / 2 - scoreLayout.width / 2, SpaceShooter.HEIGHT - BANNER_HEIGHT - 15 * 2);
        scoreFont.draw(game.batch, highscoreLayout, SpaceShooter.WIDTH / 2 - highscoreLayout.width / 2, SpaceShooter.HEIGHT - BANNER_HEIGHT - scoreLayout.height - 15 * 3);
         */
        GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont, "Try Again");
        GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "Main Menu");

        font.draw(game.batch, "Game over", 75, SpaceShooter.HEIGHT - 60);
        font2.draw(game.batch, "Score: \n" + score, 75, SpaceShooter.HEIGHT - 150);
        font2.draw(game.batch, "Highcore: \n" + highscore, 75, SpaceShooter.HEIGHT - 250);

        float tryAgainX = SpaceShooter.WIDTH / 2 - tryAgainLayout.width / 2;
        float tryAgainY = SpaceShooter.HEIGHT / 2 - tryAgainLayout.height / 2;
        float mainMenuX = SpaceShooter.WIDTH / 2 - mainMenuLayout.width / 2;
        float mainMenuY = SpaceShooter.HEIGHT / 2 - mainMenuLayout.height / 2 - tryAgainLayout.height - 15;

        float touchX = Gdx.input.getX(), touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        //Checks if hovering over try again button
        if (touchX >= tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY >= tryAgainY - tryAgainLayout.height && touchY < tryAgainY)
            tryAgainLayout.setText(scoreFont, "Try Again", Color.YELLOW, 0, Align.left, false);

        //Checks if hovering over main menu button
        if (touchX >= mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY >= mainMenuY - mainMenuLayout.height && touchY < mainMenuY)
            mainMenuLayout.setText(scoreFont, "Main Menu", Color.YELLOW, 0, Align.left, false);

        //If try again and main menu is being pressed
        if (Gdx.input.justTouched()) {
            //Try again
            if (touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY > tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainGameScreen(game));
                return;
            }

            //main menu
            if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }

        //Draw buttons
        scoreFont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY);
        scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);

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
