package com.areacontrol.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;


public class BaseDialog extends Table {

	final   Base parent;
	
	
	public BaseDialog(final Base base, Skin skin) {
		super(skin);
		this.parent = base;
		
		setPosition(500, 400);

		add(new Label("Player:"+base.getOwner(),GameGlobals.skin));
		row();
		for (BaseComponent bc : base.getComponents()) {
			if (bc instanceof BuildableBaseComponent){
				bc.makeDialog(this);
			}
		}
		add(new Label("Units to Send",GameGlobals.skin));
		row();
		for (BaseComponent bc : base.getComponents()) {
			if (!(bc instanceof BuildableBaseComponent)){
				bc.makeDialog(this);
			}
		}	
	

		
		
	}

	public void clearAll(){
		System.out.println("Clear All");
		for (Actor a : getChildren()) {
			System.out.println(a);
			removeActor(a);
		}
		
	}
	
	public final Base getBase() {
		return parent;
	}
	
}
