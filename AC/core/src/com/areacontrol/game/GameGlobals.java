package com.areacontrol.game;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameGlobals {
	public static Skin skin;
	public static BaseDialog baseDialog = null;
	public static int playerID = 1; 
	//Map<String,float> buildTime; //  = new HashMap<String,float>();
	public static void registerDialog(BaseDialog bd) {
		if (baseDialog != null){
			baseDialog.reset();
			baseDialog.addAction(Actions.removeActor());
		}
		baseDialog = bd;
		
	}
}
