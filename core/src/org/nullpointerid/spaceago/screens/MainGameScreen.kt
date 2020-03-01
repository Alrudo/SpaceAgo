package org.nullpointerid.spaceago.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import org.nullpointerid.spaceago.SpaceShooter;
import org.nullpointerid.spaceago.entities.Bullet;
import org.nullpointerid.spaceago.entities.Enemy;
import org.nullpointerid.spaceago.entities.Explosion;
import org.nullpointerid.spaceago.tools.CollisionReact;
import org.nullpointerid.spaceago.tools.MovingBackground;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameScreen implements Screen {
    private SpaceShooter game;
    private int screenWidth = SpaceShooter.WIDTH;
    private int screenHeight = SpaceShooter.HEIGHT;
    private static final float SHOOT_WAIT_TIME = 0.3f;
    private float shootTimer;
    private float asteroidSpawnTimer;
    private static final float MIN_ENEMY_SPAWN_TIME = 0.3f;
    private static final float MAX_ENEMY_SPAWN_TIME = 0.6f;
    private static final int JET_WIDTH_PIXEL = 128;
    private static final int JET_HEIGHT_PIXEL = 128;
    private static final int JET_WIDTH = JET_WIDTH_PIXEL / 3;
    private static final int JET_HEIGHT = JET_HEIGHT_PIXEL / 3;

    private Texture background;
    private Texture bulletTexture;
    private Texture jet;
    private Texture blank;
    private Cursor cursor;
    private Pixmap pm;
    private Vector2 jetLocation = new Vector2();
    private Vector2 cursorLocation = new Vector2();

    int bulletSpeed = 20;

    private List<Bullet> bulletManager;
    private List<Enemy> enemyManager;
    private List<Explosion> explosions;

    private Random random;
    private CollisionReact playerReact;

    private BitmapFont scoreFont;

    private float health = 1; // 0 = dead, 1 = full health
    private int score;

    public MainGameScreen(SpaceShooter game) {
        this.game = game;
        enemyManager = new ArrayList<>();
        bulletManager = new ArrayList<Bullet>();
        explosions = new ArrayList<>();
        random = new Random();
        asteroidSpawnTimer = random.nextFloat() * (MAX_ENEMY_SPAWN_TIME - MIN_ENEMY_SPAWN_TIME) + MIN_ENEMY_SPAWN_TIME;
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));

        playerReact = new CollisionReact(0, 0, JET_WIDTH, JET_HEIGHT);
        blank = new Texture("images/blank.png");
        score = 0;

        game.movingBackground.setFixedSpeed(false);
        game.movingBackground.setSpeed(MovingBackground.DEFAULT_SPEED);

    }

    @Override
    public void show() {
        background = new Texture("images/backg.jfif");
        jet = new Texture("images/jet1.png");
        bulletTexture = new Texture("images/bullet.png");
    }

    @Override
    public void render(float delta) {
        // move
        move();
        shootTimer += delta;
        if (Gdx.input.isButtonPressed(MouseEvent.NOBUTTON) && shootTimer >= SHOOT_WAIT_TIME) {
            shootTimer = 0;
            int xOffset = 55; // bullet exit location offset
            int yOffset = 85; // bullet exit location offset
            bulletManager.add(new Bullet(jetLocation.x + xOffset, jetLocation.y + yOffset));
        }

        //Enemy spawn
        asteroidSpawnTimer -= delta;
        if (asteroidSpawnTimer <= 0) {
            asteroidSpawnTimer = random.nextFloat() * (MAX_ENEMY_SPAWN_TIME - MIN_ENEMY_SPAWN_TIME) + MIN_ENEMY_SPAWN_TIME;
            enemyManager.add(new Enemy(random.nextInt(Gdx.graphics.getWidth() - Enemy.WIDTH)));
        }

        //Update enemies
        ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();
        for (Enemy enemy : enemyManager) {
            enemy.update(delta);
            if (enemy.remove)
                enemiesToRemove.add(enemy);
        }

        //Update bullets
        ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bulletManager) {
            bullet.update(delta);
            if (bullet.remove)
                bulletsToRemove.add(bullet);
        }

        //Update explosions
        ArrayList<Explosion> explosionsToRemove = new ArrayList<Explosion>();
        for (Explosion explosion : explosions) {
            explosion.update(delta);
            if (explosion.remove)
                explosionsToRemove.add(explosion);
        }
        explosions.removeAll(explosionsToRemove);

        //After player moves update collision
        playerReact.move(jetLocation.x, jetLocation.y);

        //After all updates check for collisions
        for (Bullet bullet : bulletManager) {
            for (Enemy enemy : enemyManager) {
                // if collision occured
                if (bullet.getCollisionRect().collidesWith(enemy.getCollisionReact())) {
                    bulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemy);
                    explosions.add(new Explosion(enemy.getX(), enemy.getY()));
                    score += 100;
                }
            }
        }
        //enemyManager.removeAll(enemiesToRemove);
        bulletManager.removeAll(bulletsToRemove);

        for (Enemy enemy : enemyManager) {
            if (enemy.getCollisionReact().collidesWith(playerReact)) {
                enemiesToRemove.add(enemy);
                health -= 0.1;

                // If dead, go to game over
                if (health <= 0) {
                    this.dispose();
                    game.setScreen(new GameOverScreen(game, score));
                }
            }
        }
        enemyManager.removeAll(enemiesToRemove);


        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        game.batch.draw(background, 0, 0);
        game.movingBackground.updateRender(delta, game.batch);

        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "" + score);
        scoreFont.draw(game.batch, scoreLayout, Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - scoreLayout.height - 10);
        game.batch.draw(jet, jetLocation.x, jetLocation.y);

        for (Bullet bullet : bulletManager) {
            bullet.render(game.batch);
        }
        for (Enemy enemy : enemyManager) {
            enemy.render(game.batch);
        }
        for (Explosion explosion : explosions) {
            explosion.render(game.batch);
        }
        if (health > 0.6f)
            game.batch.setColor(Color.GREEN);
        else if (health > 0.2f)
            game.batch.setColor(Color.ORANGE);
        else
            game.batch.setColor(Color.RED);

        game.batch.draw(blank, 0, 0, Gdx.graphics.getWidth() * health, 5);
        game.batch.setColor(Color.WHITE);

        game.batch.end();

    }

    public void move() {
        cursorLocation.x = Gdx.input.getX();
        cursorLocation.y = screenHeight - Gdx.input.getY();
        /*
        //x-axis restrict
        if (0 < cursorLocation.x &&  cursorLocation.x < SpaceShooter.WIDTH) {
            jetLocation.x = cursorLocation.x;
        }
        if (cursorLocation.x > SpaceShooter.WIDTH) {
            jetLocation.x = SpaceShooter.WIDTH - jet.getWidth();
        }
        if (cursorLocation.x < 0) {
            jetLocation.x = 0;
        }
        //y-axis restrict
        if (0 < cursorLocation.y &&  cursorLocation.y < SpaceShooter.HEIGHT) {
            jetLocation.y = cursorLocation.y;
        }
        if (cursorLocation.y > SpaceShooter.HEIGHT) {
            jetLocation.y = 500;
        }
        if (cursorLocation.y < 0) {
            jetLocation.y = jet.getHeight();
        }
         */
        jetLocation.x = cursorLocation.x - jet.getWidth() / 2;
        jetLocation.y = cursorLocation.y - jet.getHeight() / 2;
        if (!(cursorLocation.x < screenWidth & cursorLocation.x > 0 & cursorLocation.y < screenHeight & cursorLocation.y > 0)) {
            Gdx.input.setCursorCatched(false);
            if (cursorLocation.y <= 0) {
                jetLocation.y = 0;
            }
            if (cursorLocation.x <= 0) {
                jetLocation.x = 0;
            }
            if (cursorLocation.y >= screenHeight) {
                jetLocation.y = 0;
            }
            if (cursorLocation.x >= screenWidth) {
                cursorLocation.x = 0;
            }

        } else {
            //Gdx.input.setCursorCatched(true);
            /*
            //pm = new Pixmap(Gdx.files.internal("xxx.png"));
            //cursor = Gdx.graphics.newCursor(pm, 2, 2);
            //Gdx.graphics.setCursor(cursor);
            if (cursorLocation.y <= 0) {
                jetLocation.y = 0;
            }
            if (cursorLocation.x <= 0) {
                jetLocation.x = jet.getWidth();
            }
            if (jetLocation.y > screenHeight - jet.getHeight()) {
                jetLocation.y = screenHeight - jet.getHeight();
            }
            if (jetLocation.x > screenWidth - jet.getWidth()) {
                jetLocation.x = screenWidth - jet.getWidth();
            }

             */

        }
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
