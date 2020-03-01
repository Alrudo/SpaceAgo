package org.nullpointerid.spaceago;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.nullpointerid.spaceago.screens.MainMenuScreen;
import org.nullpointerid.spaceago.tools.MovingBackground;

public class SpaceShooter extends Game {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 800;
    public static final String TITLE = "SpaceAgo";

    public SpriteBatch batch;
    public MovingBackground movingBackground;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.movingBackground = new MovingBackground();
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void resize(int width, int height) {
        this.movingBackground.resize(width, height);
        super.resize(width, height);
    }
}
