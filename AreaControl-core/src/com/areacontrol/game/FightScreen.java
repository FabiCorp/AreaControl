package com.areacontrol.game;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class FightScreen implements Screen {

	protected Game  game;
	UnitContainer   uc1;
	UnitContainer   uc2;
	Screen          previous;
	Stage           stage;
	float           timeSinceUpdate;
	boolean         singlePlayerMode;
	
	Map<Unit,Actor>       actorMap;
	Map<String,Unit> unitMap;
	
	public FightScreen(Game game, UnitContainer uc1,UnitContainer uc2,Screen prev){
		this.game        = game;
		this.previous    = prev;
		this.uc1         = uc1;
		this.uc2         = uc2; 
		timeSinceUpdate  = 0;
		singlePlayerMode = true;
		
		actorMap = new HashMap<Unit, Actor>();
		unitMap  = new HashMap<String,Unit>();
		
		stage    = new Stage(); // Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
		Gdx.input.setInputProcessor(stage);
		
		Label ll = new Label("Player: "+Assets.gameInfo.getPlayerID(),Assets.skin);
		ll.setPosition(0, 400);
		stage.addActor(ll);

		int x = 300;
		for(Map.Entry<String,ArrayList<Unit>> e : uc1.getSet()){
			int y = 50;
			for (Unit u: e.getValue()){
				String  symbol = u.generateSymbol();  
				Label l = new Label(symbol,Assets.skin);
				l.setPosition(x, y);
				actorMap.put(u, l);
				if (u.getPlayerId() == Assets.gameInfo.getPlayerID()) {
					unitMap.put(u.getId(), u);
					System.out.println("Registering: "+ u.getId());
				}
				stage.addActor(l);
				y+= 50;
			}
			x -= 80;
		}
			

		x = 380;
		for(Map.Entry<String,ArrayList<Unit>> e : uc2.getSet()){
			int y = 50;
			for (Unit u: e.getValue()){
				String  symbol = u.generateSymbol();  
				Label l = new Label(symbol,Assets.skin);
				l.setPosition(x, y);
				actorMap.put(u, l); 
				if (u.getPlayerId() == Assets.gameInfo.getPlayerID()) {
					unitMap.put(u.getId(), u);
					System.out.println("Registering: "+ u.getId());
				}
				stage.addActor(l);
				y+= 50;
			}
			x += 80;
		}
		Assets.log.log(Level.INFO, "Starting Fight: " + Assets.gameInfo.getPlayerID());	
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

	
	protected void update(float delta) {
		timeSinceUpdate += delta;
		if (timeSinceUpdate > Assets.fightTime)
		{
			fightingUpdate(delta);
			timeSinceUpdate -= Assets.fightTime;
		}
	}
	
	protected void fightingUpdate(float delta) {

		// in multiplayermode only the own units fight
		if (singlePlayerMode || uc1.getPlayerID() == Assets.gameInfo.getPlayerID()) {				
			for(Map.Entry<String,ArrayList<Unit>> e : uc1.getSet()){
				for (Unit u: e.getValue()){
					// fight the units u will fight against ? 
					u.attack(uc2);
				}
			}
		}

		// in multiplayermode only the own units fight
		if (!singlePlayerMode || uc2.getPlayerID() == Assets.gameInfo.getPlayerID()) {
			for(Map.Entry<String,ArrayList<Unit>> e : uc2.getSet()){
				for (Unit u: e.getValue()){
					u.attack(uc1);
				}
			}
		}

		int uCount1 = 0;
		for(Map.Entry<String,ArrayList<Unit>> e : uc1.getSet()){
			Iterator<Unit> it = e.getValue().iterator();
			while (it.hasNext()) {
				Unit u = it.next();
				if (!u.isAlive()) {
					Actor a = actorMap.get(u);
					a.remove();
					actorMap.remove(u);
					it.remove();
				}
			}
			uCount1 += e.getValue().size();
		}

		int uCount2 = 0;
		for(Map.Entry<String,ArrayList<Unit>> e : uc2.getSet()){
			Iterator<Unit> it = e.getValue().iterator();
			while (it.hasNext()) {
				Unit u = it.next();
				if (!u.isAlive()) {
					Actor a = actorMap.get(u);
					a.remove();
					actorMap.remove(u);
					it.remove();
				}
			}
			uCount2 += e.getValue().size();
		}

		Assets.log.log(Level.FINER,"Units Left: " + uCount1 + " " + uCount2);

		for(Map.Entry<Unit,Actor> e : actorMap.entrySet()){
			e.getKey().makeSprite(e.getValue());
		}

		if (uCount1 == 0 || uCount2 == 0) {
			Assets.log.log(Level.INFO,"Fight Done: " + uCount1 + " " + uCount2);
			//game.setScreen(previous);
		}

	}

	@Override
	public void resize(int width, int height) {		
	}

	@Override
	public void pause() {		
	}

	@Override
	public void resume() {		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}
}
