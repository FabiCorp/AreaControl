package com.areacontrol.game;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import appwarp.WarpController;
import appwarp.WarpListener;
import appwarp.WarpMessage;

public class MultiPlayerFightScreen extends FightScreen implements Screen, WarpListener {

	public MultiPlayerFightScreen(Game game, UnitContainer uc1, UnitContainer uc2, Screen prev) {
		super(game, uc1, uc2, prev);
		singlePlayerMode = false;
		WarpController.getInstance().setListener(this);
		Assets.log.setLevel(Level.FINER);
	}

	@Override
	protected void update(float time) {
		timeSinceUpdate += time;
		if (timeSinceUpdate > Assets.fightTime)
		{
			fightingUpdate(timeSinceUpdate);
			timeSinceUpdate -= Assets.fightTime;

			super.update(time);
			Assets.log.log(Level.FINER,"Fightscreen update:"+time);
			if (Assets.gameInfo.getPlayerID() != uc1.getPlayerID()) {
				Assets.log.log(Level.FINER,"Fightscreen update: sending uc1-info from player: " 
						+ Assets.gameInfo.getPlayerID() + " to " + uc1.getPlayerID());
				ACUnitContainerMessage msg1 = new ACUnitContainerMessage(uc1,ACUnitContainerMessageType.InFight);
				msg1.broadcast();			
			}

			if (Assets.gameInfo.getPlayerID() != uc2.getPlayerID()) {
				Assets.log.log(Level.INFO,"Fightscreen update: sending uc2-info from player: " 
						+ Assets.gameInfo.getPlayerID() + " to " + uc2.getPlayerID());
				ACUnitContainerMessage msg1 = new ACUnitContainerMessage(uc2,ACUnitContainerMessageType.InFight);
				msg1.broadcast();			
			}
		}
		
	}
	
	@Override
	public void onWaitingStarted(String message) {
		
		
	}

	@Override
	public void onError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStarted(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished(int code, boolean isRemote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameUpdateReceived(WarpMessage msg) {
		// TODO Auto-generated method stub
		if (msg instanceof ACUnitContainerMessage){
			Assets.log.log(Level.FINER,"FightScreen Received Message for Player" + Assets.gameInfo.getPlayerID());
			UnitContainer uc1 = ((ACUnitContainerMessage) msg).getUnits();
			for(Map.Entry<String,ArrayList<Unit>> e : uc1.getSet()){
				for (Unit u: e.getValue()){
					System.out.println("Received " + u.getId());
					if (u.getPlayerId() != Assets.gameInfo.getPlayerID())
						Assets.log.log(Level.SEVERE,"Received Unit from other player" + u.getId());
					else {
						Unit localU = unitMap.get(u.getId());
						if (localU != null){
							localU.update(u);
						} else {
							System.out.println("Error: local unit with Id " + u.getId() + " not found");
						}
							
					}
				}
			}
		}
	}

}
