package ru.deathcry.spaceago.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.deathcry.spaceago.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "hello-world";
		cfg.width = 440;
		cfg.height = 800;
		new LwjglApplication(new Main(), cfg);
	}
}
