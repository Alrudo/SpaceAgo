package org.nullpointerid.spaceago.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.nullpointerid.spaceago.SpaceShooter;

public class MovingBackground {

    public static final int DEFAULT_SPEED = 80;
    private static final int ACCELERATION = 50;
    private static final int REACH_ACCELERATION = 200;

    private Texture img;
    private float y1, y2;
    private int speed; //Pixels / seconds
    private int finalSpeed;
    private float imageScale;
    private boolean fixedSpeed;

    public MovingBackground() {
        img = new Texture("images/mov_background2.png");

        y1 = 0;
        y2 = img.getHeight();
        speed = 0;
        finalSpeed = DEFAULT_SPEED;
        imageScale = SpaceShooter.WIDTH / img.getWidth();
        fixedSpeed = true;
    }

    public void updateRender(float dt, SpriteBatch batch) {
        if (speed < finalSpeed) {
            speed += REACH_ACCELERATION * dt;
            if (speed > finalSpeed) {
                speed = finalSpeed;
            }
        } else if (speed > finalSpeed) {
            speed -= REACH_ACCELERATION * dt;
            if (speed < finalSpeed) {
                speed = finalSpeed;
            }
        }

        if (!fixedSpeed) {
            speed += ACCELERATION * dt;
        }

        y1 -= speed * dt;
        y2 -= speed * dt;

        if (y1 + img.getHeight() * imageScale <= 0) {
            y1 = y2 + img.getHeight() * imageScale;
        }
        if (y2 + img.getHeight() * imageScale <= 0) {
            y2 = y1 + img.getHeight() * imageScale;
        }

        //Render
        batch.draw(img, 0, y1, SpaceShooter.WIDTH, img.getHeight() * imageScale);
        batch.draw(img, 0, y2, SpaceShooter.WIDTH, img.getHeight() * imageScale);
    }

    public void resize(int width, int height) {
        imageScale = width / img.getHeight();
    }

    public void setSpeed(int finalSpeed) {
        this.finalSpeed = finalSpeed;
    }

    public void setFixedSpeed(boolean fixedSpeed) {
        this.fixedSpeed = fixedSpeed;
    }
}
