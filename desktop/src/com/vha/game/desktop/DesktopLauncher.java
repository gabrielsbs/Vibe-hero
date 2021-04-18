package com.vha.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vha.game.IActivityRequestHandler;
import com.vha.game.VibeHero;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 400;
		config.width = 700;
		IActivityRequestHandler activityRequestHandler = new IActivityRequestHandler() {
			@Override
			public void showResponseSelector(boolean show, String answer) {

			}
		};
		new LwjglApplication(new VibeHero(activityRequestHandler, config.height, config.width), config);
	}
}
