package com.areacontrol.game;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;


public class Base extends Actor {
	Texture texture = new Texture(Gdx.files.internal("BaseIcon.png"));
	float baseX = 0, baseY = 0;
	//public boolean started = false;
	private int owner;
	
	private BaseComponentContainer unitsToSend;
	private ArrayList<BaseComponent> components;
	
	public ArrayList<BaseComponent> getComponents() {
		return components;
	}
	public Base(int x,int y){
		baseX = x;
		baseY = y;
		owner = 0;
		
		unitsToSend = new BaseComponentContainer();
		
		components = new ArrayList<BaseComponent>();
		components.add(new BaseComponentBuildable("Worker",this));
		components.add(new BaseComponentBuildable("Barracks",this));
		components.add(new BaseComponentBuildable("Research",this));
		
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
					baseComponent instanceof BaseComponentBuildable){
				BaseComponentBuildable b = (BaseComponentBuildable) baseComponent;
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
		
		ArrayList<BaseComponent> newComponents = new ArrayList<BaseComponent>();
		
		for (BaseComponent baseComponent : components) {
			ArrayList<String> enables = GameGlobals.baseComponentData.get(baseComponent.getName()).enables();
			for (String newElement : enables) {
				BaseComponent b = hasComponent(baseComponent.getName());	
				if (b != null && b.getCount()>0 && hasComponent(newElement)==null) {
					//System.out.println("Finished " + baseComponent.getName() + " making: " + newElement);
					
					
					if (GameGlobals.baseComponentData.get(newElement).isUnit()){
						BaseComponentBuildableUnit n1 = new BaseComponentBuildableUnit(newElement,this);
						newComponents.add(n1);
						BaseComponent n2 = new BaseComponentUnit(newElement,this);
						newComponents.add(n2);						
					}
					else{
						BaseComponentBuildable n1 = new BaseComponentBuildable(newElement,this);
						newComponents.add(n1);
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
		BaseComponentBuildableUnit from = null;
		BaseComponentUnit to   = null;
		
		for (BaseComponent baseComponent : components) {
			if (baseComponent.getName().equals(name) &&
					baseComponent instanceof BaseComponentBuildableUnit) {
				from = (BaseComponentBuildableUnit) baseComponent;
			}
		}
		
		for (BaseComponent baseComponent : components) {
			System.out.println(baseComponent.getName() + " is type " + (baseComponent instanceof BaseComponentUnit));
			if (baseComponent.getName().equals(name) &&
					baseComponent instanceof BaseComponentUnit) {
				to = (BaseComponentUnit) baseComponent;
			}
		}
		
		System.out.println(from + " " + to + " c: " + from.getCount());
		if (from != null && to != null && from.getCount()>0){
			Unit u = from.removeUnit();
			to.addUnit(u);
			System.out.println("Moving one " + name + " to send out");
		}
	}
	public void moveUnitFromSend(String name) {
		// TODO Auto-generated method stub
		BaseComponentUnit from          = null;
		BaseComponentBuildableUnit to   = null;
		
		for (BaseComponent baseComponent : components) {
			if (baseComponent.getName().equals(name) &&
					!(baseComponent instanceof BaseComponentUnit)) {
				from = (BaseComponentUnit) baseComponent;
			}
		}
		
		for (BaseComponent baseComponent : components) {
			if (baseComponent.getName().equals(name) &&
					(baseComponent instanceof BaseComponentBuildableUnit)) {
				to = (BaseComponentBuildableUnit) baseComponent;
			}
		}
		
		if (from != null && to != null && from.getCount()>0){
			Unit u = from.removeUnit();
			to.addUnit(u);
			System.out.println("Moving one " + name + " back to barracks");
		}
	}
	public void sendUnits() {
		// TODO Auto-generated method stub
		
		unitsToSend.activate(); // this changes the behavior of the click on the units in the base
	}
	
	
}
