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

import appwarp.WarpController;
import appwarp.WarpListener;
import appwarp.WarpMessage;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class MultiPlayerConnectScreen implements Screen, WarpListener {
	Game game;
	Label label;
	private final String tryingToConnect = "Connecting to AppWarp";
	private final String waitForOtherUser = "Waiting for Other User";
	private final String errorInConnection = "Error in Connection Go Back";
	
	private final String game_win   = "Congrats You Win!";
	private final String game_loose = "Oops You Loose!";
	private final String enemy_left = "Congrats You Win 2!";
	
	private String msg = tryingToConnect;
	
	Stage mainScreen;
	
	public MultiPlayerConnectScreen (Game game) {
		this.game = game;
		WarpController.getInstance().startApp(getRandomHexString(10));
		mainScreen = new Stage(); 
		label = new Label(msg,Assets.skin);
		label.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, Align.center);
		mainScreen.addActor(label);
		WarpController.getInstance().setListener(this);
	}

	public void update () {
		label.setText(this.msg);
		if (Gdx.input.justTouched()) {
/*			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
   Going Back here
			if (backBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
				WarpController.getInstance().handleLeave();
				return;
			}
			*/
		}
	}

	public void draw () {
		Gdx.gl.glClearColor(.0f, .255f, .255f, 1);	
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mainScreen.act(Gdx.graphics.getDeltaTime());
		mainScreen.draw();
	}

	@Override
	public void render (float delta) {
		update();
		draw();
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void show () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
	
	@Override
	public void onError (String message) {
		this.msg = errorInConnection;
		update();
	}

	@Override
	public void onGameStarted (String message) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run () {
				System.out.println("Starting Multiplayer");
				game.setScreen(new MultiplayerGameScreen(game));
			}
		});
		
	}

	@Override
	public void onGameFinished (int code, boolean isRemote) {
		if(code==WarpController.GAME_WIN){
			this.msg = game_loose;
		}else if(code==WarpController.GAME_LOOSE){
			this.msg = game_win;
		}else if(code==WarpController.ENEMY_LEFT){
			this.msg = enemy_left;
		}
		System.out.println("We are now in the Connect Screen Again");
		//update();
		game.setScreen(new MainMenuScreen(game));
	}
	
	@Override
	public void onGameUpdateReceived (WarpMessage msg) {
		System.out.println("Message Received in MP Connect");
	}

	@Override
	public void onWaitingStarted(String message) {
		this.msg = waitForOtherUser;
		update();
	}

	private String getRandomHexString(int numchars){
	      Random r = new Random();
	      StringBuffer sb = new StringBuffer();
	      while(sb.length() < numchars){
	          sb.append(Integer.toHexString(r.nextInt()));
	      }
	      return sb.toString().substring(0, numchars);
	  }
}
