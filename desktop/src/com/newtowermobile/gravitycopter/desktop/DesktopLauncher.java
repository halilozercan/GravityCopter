package com.newtowermobile.gravitycopter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.newtowermobile.gravitycopter.IActivityRequestHandler;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new GravityCopterOld(new IActivityRequestHandler() {
			@Override
			public void showAds(boolean show) {

			}
		}), config);
	}

}
