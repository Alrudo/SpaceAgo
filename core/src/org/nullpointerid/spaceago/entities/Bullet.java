package org.nullpointerid.spaceago.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.nullpointerid.spaceago.SpaceShooter;
import org.nullpointerid.spaceago.tools.CollisionReact;

public class Bullet {
    public static final int WIDTH = 3;
    public static final int HEIGHT = 12;
    public static final int SPEED = 500;
    private static Texture texture;

    float x, y;
    CollisionReact react;
    public boolean remove = false;

    public Bullet(float x, float y) {
        this.x = x;
        this.y = y;
        this.react = new CollisionReact(x, y, WIDTH, HEIGHT);

        if (texture == null)
            texture = new Texture("images/bullet.png");
    }

    public void update(float deltaTime) {
        y += SPEED * deltaTime;
        if (y > SpaceShooter.HEIGHT)
            remove = true;

        react.move(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public CollisionReact getCollisionRect() {
        return react;
    }

}
/*
public class Bullet {
    public Vector2 bulletLocation = new Vector2(0, 0);
    private Vector2 bulletVelocity = new Vector2(0, 0);
    public final static int WIDTH = 16;
    public final static int HEIGHT = 16;
    CollisionReact react;

    public Bullet(Vector2 sentLocation, Vector2 sentVelocity)
    {
        bulletLocation = new Vector2(sentLocation.x, sentLocation.y);
        bulletVelocity = new Vector2(sentVelocity.x, sentVelocity.y);
        this.react = new CollisionReact(bulletLocation.x, bulletLocation.y, WIDTH, HEIGHT);
    }

    public void Update()
    {
        bulletLocation.x += bulletVelocity.x;
        bulletLocation.y += bulletVelocity.y;
        react.move(bulletLocation.x, bulletLocation.y);
    }

    public CollisionReact getCollisionReact () {
        return react;
    }
}
 */
