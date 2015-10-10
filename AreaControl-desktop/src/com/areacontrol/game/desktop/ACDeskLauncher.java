package com.areacontrol.game.desktop;

import com.areacontrol.game.AreaControl;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class ACDeskLauncher {
	public static void main (String[] arg) {
		try {
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			new LwjglApplication(new AreaControl(), config);
		} catch (RuntimeException e) {
			System.out.println("Caught: " + e);
		}
		
	}
}
