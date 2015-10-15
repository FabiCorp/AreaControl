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
import java.util.Map;
import java.util.logging.Level;

import appwarp.WarpController;
import appwarp.WarpListener;
import appwarp.WarpMessage;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class MultiplayerGameScreen extends GameScreen implements Screen, WarpListener {

	UnitContainer attacker;
	UnitContainer defender;
	boolean       startFight;
	boolean       testMode;
	public MultiplayerGameScreen (Game game) {
		super(game);
		WarpController.getInstance().setListener(this);
		startFight = false;
		testMode   = false;
	}
	
	@Override
	public void checkUnitsArrived() {
		// multiplayer Version
		for (UnitContainer units : unitsMoving) {
			if (units.haveArrived()){
				attacker = units;
				Assets.log.log(Level.INFO,"Player " + Assets.gameInfo.getPlayerID()+" (Attacker) has units arriving, sends message");
				if (testMode) {
					ACStringMessage message = new ACStringMessage("M"+Assets.gameInfo.getPlayerID());
					message.broadcast();
				}else {
				ACUnitContainerMessage message = new ACUnitContainerMessage(units,ACUnitContainerMessageType.PreFightFromAttacker);
				message.broadcast();
				}
				units.setArrivedFalse();
			}	
		}	
	}
	
	@Override
	public void update(float time) {
		super.update(time);
		
		if (startFight) {
			startFight = false;
			Assets.log.log(Level.INFO,"Player " + Assets.gameInfo.getPlayerID()+" starting FightScreen");
			game.setScreen(new MultiPlayerFightScreen(game, attacker, defender, this));
		}	
	};
	
	@Override
	protected void gameOver() {
		System.out.println("Local User: " + WarpController.getInstance().getLocalUser() + "terminating");
		ACStringMessage msg = new ACStringMessage("GameOver");
		msg.broadcast();
		WarpController.getInstance().handleLeave();
		gameOverLocal();
		
	}

	@Override
	public void onWaitingStarted (String message) {
	}

	@Override
	public void onError (String message) {
	}

	@Override
	public void onGameStarted (String message) {
		
	}

	@Override
	public void onGameFinished (int code, boolean isRemote) {
		if(isRemote){
			System.out.println("Detected Remote");
		}else{
			if(code==WarpController.GAME_WIN){
				System.out.println("WIN MESSAGE");
				//world.state = World.WORLD_STATE_NEXT_LEVEL;
			}else if(code==WarpController.GAME_LOOSE){
				System.out.println("LOOSE MESSAGE");
				//world.state = World.WORLD_STATE_GAME_OVER;
			}
		}
		WarpController.getInstance().handleLeave();
		gameOverLocal();
	}

	@Override
	public void onGameUpdateReceived (WarpMessage message) {
		
		Assets.log.log(Level.INFO,"Player: " + Assets.gameInfo.getPlayerID());
		
		if (message instanceof ACStringMessage) {
			ACStringMessage msg = (ACStringMessage) message;
			Assets.log.log(Level.INFO,"Game Over Message Received" + msg.getString());
			ACStringMessage msg2 = new ACStringMessage(msg.getString()+"."+Assets.gameInfo.getPlayerID());
			msg2.broadcast();
		}
		else if (message instanceof ACUnitContainerMessage){
			// we are prefight
			ACUnitContainerMessage msg = (ACUnitContainerMessage) message;
			Assets.log.log(Level.INFO,"Player: " + Assets.gameInfo.getPlayerID() + " received message of type " + msg.getMessageType());
			switch(msg.getMessageType()) {
			case PreFightFromAttacker:
				attacker = msg.getUnits();
				Assets.log.log(Level.INFO,"Player: " + Assets.gameInfo.getPlayerID() + " received attackers UnitContainer to start fight");
				for(Map.Entry<String,ArrayList<Unit>> e : attacker.getSet()){
					for (Unit u: e.getValue()){
						Assets.log.log(Level.FINER,"Unit:"+u.getName());
					}
				}
				Base defBase  = findBase(attacker.getTagetBaseID());
				defender = defBase.getUnits();
				Assets.log.log(Level.INFO,"Player: " + Assets.gameInfo.getPlayerID() + " sends defenders UnitContainer");
				for(Map.Entry<String,ArrayList<Unit>> e : defender.getSet()){
					for (Unit u: e.getValue()){
						Assets.log.log(Level.FINER,"Unit:"+u.getName());
					}
				}
				ACUnitContainerMessage msg2 = new ACUnitContainerMessage(defender,ACUnitContainerMessageType.PreFightFromDefender);
				msg2.broadcast();
				startFight = true;
				break;
			case PreFightFromDefender:
				// attacker must be set in the update loop
				defender = msg.getUnits();
				Assets.log.log(Level.INFO,"Player: " + Assets.gameInfo.getPlayerID() + " received defenders UnitContainer");
				for(Map.Entry<String,ArrayList<Unit>> e : defender.getSet()){
					for (Unit u: e.getValue()){
						Assets.log.log(Level.FINEST,"Unit:"+u.getName());
					}
				}
				startFight = true;
				break;
			default:
				Assets.log.log(Level.INFO,"Player: " + Assets.gameInfo.getPlayerID() + " received illegal Message" + msg.getMessageType());
				break;
			}
		}
	}
}


