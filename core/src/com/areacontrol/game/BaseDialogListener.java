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
		
		GameGlobals.registerDialog(bd);
		myBase.getStage().addActor(bd);
		
		return true;
	}
}
