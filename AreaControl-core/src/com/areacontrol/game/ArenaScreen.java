package com.areacontrol.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class ArenaScreen implements Screen {

	protected Game  game;
	protected Stage stage;
	
	public ArenaScreen(Game game) {
		this.game  = game;
		stage = new Stage(); // Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
	    Assets.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		Gdx.input.setInputProcessor(stage);

		UnitContainer uc1 = new UnitContainer();
		UnitContainer uc2 = new UnitContainer();
		
		for (int i = 0; i < 5; i++)
			uc1.addUnits(new Unit("Marine"));
		
		for (int i = 0; i < 2; i++)
			uc2.addUnits(new Unit("Marine"));
		uc2.addUnits(new Unit("Tank"));
		
		game.setScreen(new FightScreen(game, uc1, uc2, this));
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
