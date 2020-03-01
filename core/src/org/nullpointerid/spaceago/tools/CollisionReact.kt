package org.nullpointerid.spaceago.tools;

public class CollisionReact {

    private float x, y;
    private int width, height;

    public CollisionReact(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean collidesWith(CollisionReact react) {
        return x < react.x + react.width && y < react.y + react.height && x + width > react.x && y + height > react.y;
    }
}
