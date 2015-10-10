package com.areacontrol.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AreaControl extends Game {
	@Override
	public void create () {
            Assets.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
            setScreen(new MainMenuScreen(this));
	}
	
	@Override
	public void render() {
		super.render();
	}

	/** {@link Game#dispose()} only calls {@link Screen#hide()} so you need to override {@link Game#dispose()} in order to call
	 * {@link Screen#dispose()} on each of your screens which still need to dispose of their resources. SuperJumper doesn't
	 * actually have such resources so this is only to complete the example. */
	@Override
	public void dispose () {
		super.dispose();
		getScreen().dispose();
	}
	
	
}

