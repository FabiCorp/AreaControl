package com.areacontrol.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class BaseDialogListener extends ClickListener {
	private Base  myBase;
	
	public BaseDialogListener(Base base){
		myBase = base;
	}
	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		
		BaseDialog bd = new BaseDialog(myBase,GameGlobals.skin);
		//bd.setPosition(200, 200);
		GameGlobals.registerDialog(bd);
		myBase.getStage().addActor(bd);
		
		
		//dialog = new Dialog("Hello",stage.uiSkin);
		//((Base)event.getTarget()).started = true;
		return true;
	}
}
