package com.areacontrol.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class BaseDialog extends Table {

	
	public BaseDialog(final Base base, Skin skin) {
		super(skin);
		setPosition(base.getX()+ base.getWidth(), base.getY() + 50);
		for (BaseComponent bc : base.getComponents()) {
			final String label = bc.getName();
			final String count = "" + bc.getCount();
			
			final Label  clabel = new Label(count, skin);
			TextButton item = new TextButton(label,skin);
			item.addListener(new ClickListener() {		
				@Override
				public void clicked(InputEvent event, float x, float y) {
					int newcount = base.takeAction(label);
					final String ns = ""+newcount;
					clabel.setText(ns);
					//((Label)event.getTarget()).setText(newLabel);
					
				}
			});
			add(item);
			add(clabel);
			row();
		}	
	}

	public void clearAll(){
		System.out.println("Clear All");
		for (Actor a : getChildren()) {
			System.out.println(a);
			removeActor(a);
		}
		
	}
	
}
