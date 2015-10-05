package com.areacontrol.game;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Base extends Actor {
	Texture texture = new Texture(Gdx.files.internal("BaseIcon.png"));
	float baseX = 0, baseY = 0;
	//public boolean started = false;
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
		components.add(new BuildableBaseComponent("Worker",this));
		components.add(new BuildableBaseComponent("Barracks",this));
		components.add(new BuildableBaseComponent("Research",this));
		
		setBounds(baseX,baseY,texture.getWidth(),texture.getHeight());
		
		addListener(new BaseDialogListener(this)); 
		setTouchable(Touchable.enabled);
	}
	@Override
	public void draw(Batch batch, float alpha){
		batch.draw(texture,baseX,baseY,100,100);
	}

	public void takeAction(String string) {
		for (BaseComponent baseComponent : components) {
			if (string.equals(baseComponent.getName()) && 
					baseComponent instanceof BuildableBaseComponent){
				BuildableBaseComponent b = (BuildableBaseComponent) baseComponent;
				b.initiateBuild();
			}
		}
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	
	public int getWorkers() {
		for (BaseComponent baseComponent : components) {
			if (baseComponent.getName().equals("Worker")){
				return baseComponent.getCount();
			}
		}
	//	throw (IllegalStateException);
		return 0;
	}

	public BaseComponent hasComponent(String s){
		for (BaseComponent baseComponent : components) {
			if (baseComponent.getName().equals(s)) {
				return baseComponent;
			}
		}
		return null;
	}
	
	public void update() {
		for (BaseComponent baseComponent : components) {
			baseComponent.update();
		}
		// check if new baseComponents can be made
		
		boolean created = false;
		
		ArrayList<BaseComponent> newComponents = new ArrayList<>();
		
		for (BaseComponent baseComponent : components) {
			ArrayList<String> enables = GameGlobals.baseComponentData.get(baseComponent.getName()).enables();
			for (String newElement : enables) {
				BaseComponent b = hasComponent(baseComponent.getName());	
				if (b != null && b.getCount()>0 && hasComponent(newElement)==null) {
					//System.out.println("Finished " + baseComponent.getName() + " making: " + newElement);
					BuildableBaseComponent n1 = new BuildableBaseComponent(newElement,this);
					newComponents.add(n1);
					if (GameGlobals.baseComponentData.get(newElement).isUnit()){
						BaseComponent n2 = new BaseComponent(newElement,this);
						newComponents.add(n2);						
					}
					
					created = true;
				}
			}
		}
		
		for (BaseComponent baseComponent : newComponents)
			components.add(baseComponent);
		
		if (created && GameGlobals.baseDialog.getBase()==this){
			BaseDialog bd = new BaseDialog(this, GameGlobals.skin);
			GameGlobals.registerDialog(bd);
			getStage().addActor(bd);
		}
	}
	
	public void moveUnitToSend(String name) {
		// TODO Auto-generated method stub
		BaseComponent from = null;
		BaseComponent to   = null;
		
		for (BaseComponent baseComponent : components) {
			if (baseComponent.getName().equals(name) &&
					baseComponent instanceof BuildableBaseComponent) {
				from = baseComponent;
			}
		}
		
		for (BaseComponent baseComponent : components) {
			if (baseComponent.getName().equals(name) &&
					!(baseComponent instanceof BuildableBaseComponent)) {
				to = baseComponent;
			}
		}
		
		if (from != null && to != null && from.getCount()>0){
			from.decreaseCount();
			to.increaseCount();
			System.out.println("Moving one " + name + " to send out");
		}
	}
	public void moveUnitFromSend(String name) {
		// TODO Auto-generated method stub
		BaseComponent from = null;
		BaseComponent to   = null;
		
		for (BaseComponent baseComponent : components) {
			if (baseComponent.getName().equals(name) &&
					!(baseComponent instanceof BuildableBaseComponent)) {
				from = baseComponent;
			}
		}
		
		for (BaseComponent baseComponent : components) {
			if (baseComponent.getName().equals(name) &&
					(baseComponent instanceof BuildableBaseComponent)) {
				to = baseComponent;
			}
		}
		
		if (from != null && to != null && from.getCount()>0){
			from.decreaseCount();
			to.increaseCount();
			System.out.println("Moving one " + name + " back to barracks");
		}
	}
	
	
}
