/*
 * Copyright & License
 * 
 * Copyright by Wolfgang Wenzel
 * 
 * LICENSE
 * 
 * IMPORTANT: This license Agreement is a legal agreement between you, the
 * LICENSEE (either an individual or an entity), and the copyright holder and
 * owner of this code, the LICENSOR. By copying, running, accessing or installing
 * this software you accept the following:
 * 
 * Software License
 * 
 * GRANT OF LICENSE. Use of this software is prohibited for without a written
 * license agreement between you and the copyright owner. This software is 
 * owned by the copyright holder and ownership is protected by the copyright 
 * laws of the Federal Republic of Germany and by international treaty 
 * provisions. Upon expiration or termination of this Agreement, you shall 
 * promptly return all copies of the Software and accompanying written 
 * materials to the copyright owner. 
 * MODIFICATIONS AND DERIVATIVE WORKS. You may not modify the software or 
 * use it to create derivative works. You may not distribute such modified 
 * or derivative software to others outside of your site without written 
 * permission of the copyright owner.
 * ASSIGNMENT RESTRICTIONS. You shall not use the Software (or any part 
 * thereof) in connection with the provision of consultancy, game development
 * or other services, whether for value or otherwise, on behalf of any 
 * third party who does not hold a current valid Software License Agreement. 
 * You shall not use the Software to write other software that duplicates 
 * the functionality of the Software. You shall not rent, lease, or 
 * otherwise sublet the Software or any part thereof.
 * LIMITED WARRANTY. LICENSEE acknowledges that LICENSORS make no warranty,
 * expressed or implied, that the program will function without error, or
 * in any particular hardware environment, or so as to generate any
 * particular function or result, and further excluding any other warranty,
 * as to the condition of the program, its merchantability, or its fitness
 * for a particular purpose. LICENSORS shall not be liable for any direct,
 * consequential, or other damages suffered by the LICENSEE or any others
 * as a result of their use of the program, whether or not the same could
 * have been foreseen by LICENSORS prior to granting this License. In no
 * event shall LICENSORS liability for any breach of this agreement exceed
 * the fee paid for the license.
 * 
 * LICENSORS LIABILITY. In no event shall the LICENSOR be liable for 
 * any indirect, special, or consequential damages, such as, but not 
 * limited to, loss of anticipated profits or other economic loss in 
 * connection with or arising out of the use of the software by you or 
 * the services provided for in this Agreement, even if the LICENSOR
 * has been advised of the possibility of such damages. The LICENSOR
 * entire liability and your exclusive remedy shall be, at 
 * LICENSORS discretion, to return the Software and proof of purchase 
 * for either (a) return of any license fee, or (b) correction or 
 * replacement of Software that does not meet the terms of this 
 * limited warranty.
 * NO OTHER WARRANTIES. The LICENSOR disclaims other implied warranties, 
 * including, but not limited to, implied warranties of merchantability 
 * or fitness for any purpose, and implied warranties arising by usage 
 * of trade, course of dealing, or course of performance. Some states do 
 * not allow the limitation of the duration or liability of implied 
 * warranties, so the above restrictions might not apply to you.
 * LICENSE FEE. All individuals or organizations wishing to license this
 * software shall contact: wenzel.int@gmail.com and request a quote for
 *  a license.
 *  This license explicitly does not cover the external and linked software.
 */
package com.areacontrol.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import appwarp.WarpController;


public class GameScreen implements Screen {

	protected Game  game;
	protected Stage mainScreen;
	
	GameState       gameState;
	ArrayList<Base> bases;
	
	Label           resCount;
	private ArrayList<UnitContainer> unitsMoving;
	
	public GameScreen(Game game) {
		this.game  = game;
		mainScreen = new Stage(); // Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
	    Assets.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		Gdx.input.setInputProcessor(mainScreen);

		mapCreator();

		unitsMoving = new ArrayList<UnitContainer>();

		Assets.resources = 10000;

		Map<String,Float> buildTime = new HashMap<String,Float>();
		buildTime.put("Worker",10.0f);

		resCount = new Label("Resources" + Assets.resources, Assets.skin);
		resCount.setPosition(0, 300);
		mainScreen.addActor(resCount);

//		new Timer().scheduleTask(new Task() {	
//			@Override
//			public void run() {
//				update();
//			}
//		},0.0f,Assets.refreshTime,10000000);

		mainScreen.addListener(new InputListener(){
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				System.out.println("pressed:"+keycode);
				return true;

			}
		});

	}
	
	public void update(float time)
	{	
		for (UnitContainer units : unitsMoving) {
			units.update(time);
		}
		
		for (Base base : bases) {
			if (base.getOwner()==Assets.playerID) {
				Assets.resources +=  base.getWorkers() * Assets.resourceRatePerWorker*time;
			}
			base.update(time);
		}
		resCount.setText("Resources: " + (int) Assets.resources);
	}
	
	public void mapCreator()
	{
		bases = new ArrayList<Base>();
		
		Base b1 = new Base(0,0,this);
		if (Assets.playerID == 1)
			b1.setOwner(Assets.playerID);
		mainScreen.addActor(b1);
		bases.add(b1);
		
		b1 = new Base(150,150,this);
		mainScreen.addActor(b1);
		bases.add(b1);
		
		b1 = new Base(300,0,this);
		if (Assets.playerID == 2)
			b1.setOwner(Assets.playerID);
		mainScreen.addActor(b1);
		bases.add(b1);
		
		b1 = new Base(150,300,this);
		mainScreen.addActor(b1);
		bases.add(b1);
	}
    @Override
    public void dispose() {
        mainScreen.dispose();
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

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void register(UnitContainer units) {
		unitsMoving.add(units);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		update(delta);
		if (gameState instanceof GameStateSendUnits){
    		Gdx.gl.glClearColor(.35f, .206f, .235f, 1);
    	} 
    	else {    		
    		Gdx.gl.glClearColor(.0f, .255f, .255f, 1);	
    	}
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainScreen.act(Gdx.graphics.getDeltaTime());
        mainScreen.draw();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	protected void gameOver() {
		game.setScreen(new MainMenuScreen(game));
	}
   
	protected void clickedButton(){
		// sendLocation(0, 0, 0, 0);
	}
	
}
