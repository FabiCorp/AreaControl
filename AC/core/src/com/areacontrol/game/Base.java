package com.areacontrol.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


public class Base extends Actor {
	Texture texture = new Texture(Gdx.files.internal("BaseIcon.png"));
	float baseX = 0, baseY = 0;
	public boolean started = false;
	private int owner;
		
	private ArrayList<BaseComponent> components;
	
	public ArrayList<BaseComponent> getComponents() {
		return components;
	}
	
	public Base(int x,int y){
		baseX = x;
		baseY = y;
		owner = 0;
		components = new ArrayList<BaseComponent>();
		components.add(new BaseComponent("Worker"));
		components.add(new BaseComponent("Barracks"));
		components.add(new BaseComponent("Factory"));
		setBounds(baseX,baseY,texture.getWidth(),texture.getHeight());
		
		addListener(new BaseDialogListener(this)); 
		setTouchable(Touchable.enabled);
	}
	@Override
	public void draw(Batch batch, float alpha){
		batch.draw(texture,baseX,baseY,100,100);
	}

	@Override
	public void act(float delta){
		if(started){
			baseX+=5;
		}
	}
	
	
	public int takeAction(String string) {
		for (BaseComponent baseComponent : components) {
			if (string.equals(baseComponent.getName())){
				baseComponent.increaseCount();
				if (baseComponent.equals("Barracks") && baseComponent.getCount()==1){
					components.add(new BaseComponent("Infantry"));
				}
				return baseComponent.getCount();
			}
		}
		// TODO Auto-generated method stub
		return -1;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}
	
	
}
