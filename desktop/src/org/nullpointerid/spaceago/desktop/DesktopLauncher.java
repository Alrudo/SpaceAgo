package org.nullpointerid.spaceago.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.nullpointerid.spaceago.SpaceShooter;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = SpaceShooter.WIDTH;
        config.height = SpaceShooter.HEIGHT;
        config.title = SpaceShooter.TITLE;
        config.resizable = false;
        new LwjglApplication(new SpaceShooter(), config);
    }
}
