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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;


public class Base extends Actor {
	private Texture texture = new Texture(Gdx.files.internal("BaseIcon.png"));
	private float baseX = 0, baseY = 0;
	//public boolean started = false;
	private int owner;
	private GameScreen game;
	
	private ArrayList<BaseComponent> components;
	
	public ArrayList<BaseComponent> getComponents() {
		return components;
	}
	public Base(int x,int y,GameScreen game){
		this.game = game;
		baseX = x;
		baseY = y;
		owner = 0;
					
		components = new ArrayList<BaseComponent>();
		components.add(new BaseComponentBuildable("Worker",this));
		components.add(new BaseComponentBuildable("Barracks",this));
		components.add(new BaseComponentBuildable("Research",this));
		
		setBounds(baseX,baseY,texture.getWidth(),texture.getHeight());
		
		addListener(new MainScreenListener(this,game)); 
		setTouchable(Touchable.enabled);
	}
	
	@Override
	public void draw(Batch batch, float alpha){
		batch.draw(texture,baseX,baseY,100,100);
	}

	public void takeAction(String string) {
		for (BaseComponent baseComponent : components) {
			if (string.equals(baseComponent.getName()))
				baseComponent.initiateBuild();
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
	
	public void update(float time) {
				
		for (BaseComponent baseComponent : components) {
			baseComponent.update(time);
		}
		// check if new baseComponents can be made
		
		boolean created = false;
		
		ArrayList<BaseComponent> newComponents = new ArrayList<BaseComponent>();
		
		for (BaseComponent baseComponent : components) {
			ArrayList<String> enables = Assets.baseComponentData.get(baseComponent.getName()).enables();
			for (String newElement : enables) {
				BaseComponent b = hasComponent(baseComponent.getName());	
				if (b != null && b.getCount()>0 && hasComponent(newElement)==null) {
					//System.out.println("Finished " + baseComponent.getName() + " making: " + newElement);
					
					
					if (Assets.baseComponentData.get(newElement).isUnit()){
						BaseComponentBuildableUnit n1 = new BaseComponentBuildableUnit(newElement,this);
						newComponents.add(n1);
						newComponents.add(n1.getUnitStore());						
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
		
		if (created && Assets.hasBaseDialog() && Assets.getBaseDialog().getBase()==this){
			BaseDialog bd = new BaseDialog(this);
			Assets.registerDialog(bd);
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
		UnitContainer unitsToSend = new UnitContainer();
		for (BaseComponent baseComponent : components) {
			if (baseComponent instanceof BaseComponentUnit){
				((BaseComponentUnit) baseComponent).moveUnits(unitsToSend);
			}
		}
		unitsToSend.setMovingFrom(this);
		game.setGameState(new GameStateSendUnits(unitsToSend)); 
	}
	
	public void addComponent(BaseComponentUnit unitStore) {
		// TODO Auto-generated method stub
		components.add(unitStore);
	}
	public GameScreen getGame() {
		return game;
	}
	
	
}
