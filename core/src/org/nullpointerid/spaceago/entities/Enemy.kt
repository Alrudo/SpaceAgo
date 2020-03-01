package org.nullpointerid.spaceago.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.nullpointerid.spaceago.SpaceShooter;
import org.nullpointerid.spaceago.tools.CollisionReact;

public class Enemy {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int SPEED = 250;
    private static Texture texture;

    private float x, y;
    private CollisionReact react;
    public boolean remove = false;

    public Enemy(float x) {
        this.x = x;
        this.y = SpaceShooter.HEIGHT;
        this.react = new CollisionReact(x, y, WIDTH, HEIGHT);

        if (texture == null)
            texture = new Texture("images/corona1.png");
    }

    public void update(float deltaTime) {
        y -= SPEED * deltaTime;
        if (y < -HEIGHT)
            remove = true;

        react.move(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, WIDTH, HEIGHT);
    }

    public CollisionReact getCollisionReact() {
        return react;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
