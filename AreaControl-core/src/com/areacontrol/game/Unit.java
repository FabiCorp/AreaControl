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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Unit extends UniqeUnitID implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	float  health;
	

	public Unit(String name, int playerID) {
		super(playerID);
		this.name   = name;
		this.health = Assets.baseComponentData.get(name).getHealth();
	}
	
	public String getName() {
		return name;
	}

	public String generateSymbol() {
		int hh = (int) health;
		if (hh<0)
			hh = 0;
		return name.substring(0,1)+hh;
	}
	
	public String getId(){
		return getName()+playerID + "."+unitID;
	}
	
	public void attack(Unit u){
		//System.out.println("Unit: " + getId() + " attacks " + u.getId() + " H:"  + (int) u.health);
		float damage = Assets.randGen.nextFloat()*10; // take some shot
		u.receiveDamage(damage);
	}

	private void receiveDamage(float damage) {	
		health -= damage;
	}

	public void attack(UnitContainer uc2) {
		// This unit: this.getName() will pick some other unit to attack

		if (getName() == "Marine"){
			// are there other marines left ? 
			ArrayList<Unit> marines = uc2.getUnits("Marine");
			// if yes: attack random marine 
			if (marines!= null && marines.size()>0){
				int i = Assets.randGen.nextInt(marines.size());
				attack(marines.get(i));	
			}
			else {
				for(Map.Entry<String,ArrayList<Unit>> e : uc2.getSet()){
					if (e.getKey() != "Marine"){
						if (e.getValue().size()>0){
							int i = Assets.randGen.nextInt(e.getValue().size());
							attack(e.getValue().get(i));
							break; // found someone to attack
						}
					}
				}
			}
		} else if (getName()=="Tank") {
			// if marines are left, attack 3 of them
			ArrayList<Unit> marines = uc2.getUnits("Marine");
			// if yes: attack random marine 
			if (marines!=null && marines.size()>0){
				for(int k=0;k<3;k++){
					int i = Assets.randGen.nextInt(marines.size());
					attack(marines.get(i));						
				}
			}
		}
	}

	public void makeSprite(Actor actor) {
		try {
			Label l = (Label) actor;
			l.setText(generateSymbol());
		} catch (Exception e) {
			System.out.println("FightScreen cannot update Actor for Unit");
		}
		
		
	}

	public boolean isAlive() {
		return health>0;
	}

	public void update(Unit u) {
		this.health = u.health;
	}
}
