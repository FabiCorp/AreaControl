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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;


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
		
		TextButton button;
		float ypos        =  0.75f*Gdx.graphics.getHeight();
		final Game myGame = this.game;
		button = new TextButton("Start",Assets.skin);
		button.setPosition(Gdx.graphics.getWidth()/2,ypos, Align.center);
		button.addListener(new ClickListener(){
            @Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	System.out.println("Starting Fight");
            	// myGame.setScreen(new FightScreen(myGame, uc1, uc2, this));
				return true;
			}
		});
		stage.addActor(button);
		ypos -= 2*button.getHeight();
	
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.0f, .255f, .255f, 1);	
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw(); 
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
