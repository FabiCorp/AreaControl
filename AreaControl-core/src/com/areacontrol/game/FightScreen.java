package com.areacontrol.game;


import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class FightScreen implements Screen {

	protected Game  game;
	UnitContainer   uc1;
	UnitContainer   uc2;
	Screen          previous;
	Stage           stage;
	float           timeSinceUpdate;
	public FightScreen(Game game, UnitContainer uc1,UnitContainer uc2,Screen prev){
		this.game       = game;
		this.uc1        = uc1;
		this.uc2        = uc2; 
		timeSinceUpdate = 0;
		
		stage    = new Stage(); // Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
	  
		Gdx.input.setInputProcessor(stage);
		

		int y = 400;
		for(Map.Entry<String,ArrayList<Unit>> e : uc1.getSet()){
			int x = 50;
			for (Unit u: e.getValue()){
				String  symbol = u.generateSymbol();  
				Label l = new Label(symbol,Assets.skin);
				l.setPosition(x, y);
				u.registerActor(l);
				stage.addActor(l);
				x+= 50;
			}
			y -= 30;
		}
			

		y = 400;
		for(Map.Entry<String,ArrayList<Unit>> e : uc2.getSet()){
			int x = 250;
			for (Unit u: e.getValue()){
				String  symbol = u.generateSymbol();  
				Label l = new Label(symbol,Assets.skin);
				l.setPosition(x, y);
				u.registerActor(l);
				stage.addActor(l);
				x+= 50;
			}
			y -= 30;
		}
			
	}
	
	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	private void update(float delta) {
		timeSinceUpdate += delta;
		if (timeSinceUpdate > Assets.fightTime){
			timeSinceUpdate -= Assets.fightTime;
			
			for(Map.Entry<String,ArrayList<Unit>> e : uc1.getSet()){
				for (Unit u: e.getValue()){
					// fight the units u will fight against ? 
					u.attack(uc2);
				}
			}
			
			for(Map.Entry<String,ArrayList<Unit>> e : uc2.getSet()){
				for (Unit u: e.getValue()){
					u.attack(uc1);
				}
			}
		}
	}

	@Override
	public void resize(int width, int height) {		
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
