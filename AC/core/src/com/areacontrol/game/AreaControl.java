package com.areacontrol.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
//import com.badlogic.gdx.utils.Timer.Task;


public class AreaControl extends ApplicationAdapter implements ApplicationListener {
	
	Stage mainScreen;
	Skin  skin;
	private int resources;
	ArrayList<Base> bases;
	Label resCount;
	@Override
	public void create () {
		mainScreen = new Stage(); // Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
	    GameGlobals.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		Gdx.input.setInputProcessor(mainScreen);	
		mapCreator();
	
		resources = 0;
		
		resCount = new Label("Resources" + resources, GameGlobals.skin);
		resCount.setPosition(0, 300);
		mainScreen.addActor(resCount);
		
		new Timer().scheduleTask(new Task() {	
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				 for (Base base : bases) {
//					if (base.getOwner()==GameGlobals.playerID) {
//						
//					}
//				}
				 resources += 100;
				 resCount.setText("Resources" + resources);
			}
		},1.0f,0.2f,10000000);
		
		
		
		mainScreen.addListener(new InputListener(){
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				System.out.println("pressed:"+keycode);
				return true;
				
			}
		});
	}
	    
	public void mapCreator()
	{
		ArrayList<Base> bases = new ArrayList<Base>();
		
		Base b1 = new Base(0,0);
		b1.setOwner(GameGlobals.playerID);
		mainScreen.addActor(b1);
		bases.add(b1);
		
		b1 = new Base(150,150);
		mainScreen.addActor(b1);
		bases.add(b1);
		
		b1 = new Base(300,0);
		mainScreen.addActor(b1);
		bases.add(b1);
		
		b1 = new Base(150,300);
		mainScreen.addActor(b1);
		bases.add(b1);
	}
    @Override
    public void dispose() {
        mainScreen.dispose();
    }

    @Override
    public void render() {    
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainScreen.act(Gdx.graphics.getDeltaTime());
        mainScreen.draw();
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
   
	
}
