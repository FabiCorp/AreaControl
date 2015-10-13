/*
 * Copyright & License
 * 
 * Copyright by Fabian and Wolfgang Wenzel
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
 *  
 *  This license explicitly does not cover the external and linked software.
 */

package com.areacontrol.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public abstract class BaseComponent {

	protected String name;

	protected Base parent;
	protected Map<String,Label> elements;
	
	int     queued; 
	float   timeLeft;
	boolean inProgress;
	
	public BaseComponent(String name, Base parent) {
		this.name   = name;
		this.parent = parent;
		
		queued       =     0;
		timeLeft     =     0;
		inProgress   = false;
		
		elements    = new HashMap<String,Label>();
	}

	
	public void makeDialog(BaseDialog baseDialog) {
		String label = getName();
		
		if (parent.isOwnedByPlayer() && canBuild()){
			label += "(B)";
		}
		
		if (parent.isOwnedByPlayer()){
			TextButton item = new TextButton(label,baseDialog.getSkin());
			item.addListener(new ClickListener() {		
				@Override
				public void clicked(InputEvent event, float x, float y) {
					parent.takeAction(name);
				}
			});
			
			baseDialog.add(item);
			register("Name", item.getLabel());
		} else
		{
			Label item = new Label(label,baseDialog.getSkin());		
			baseDialog.add(item);
			register("Name", item);
		}
			
		final Label  rlabel = new Label(" "+(int) timeLeft, baseDialog.getSkin());
		final Label  qlabel = new Label(" "+queued, baseDialog.getSkin());
		
		if (Assets.baseComponentData.get(name).isUnit() && parent.isOwnedByPlayer()){
			TextButton clabel = new TextButton(" "+getCount(), baseDialog.getSkin());
			baseDialog.add(clabel);
			register("Count", clabel.getLabel());
			clabel.addListener(new ClickListener() {		
				@Override
				public void clicked(InputEvent event, float x, float y) {
					moveUnit();
				}
			});
		}
		else
		{
			Label  clabel = new Label(" "+getCount(), baseDialog.getSkin());
			baseDialog.add(clabel);
			register("Count", clabel);
		}
				
		baseDialog.add(qlabel);
		register("Queue", qlabel);
		baseDialog.add(rlabel);
		register("Time", rlabel);
		baseDialog.row();
	}
	
	public boolean canBuild() {
		return Assets.gameInfo.getResources() > Assets.baseComponentData.get(name).getResourceCost();
	}
	public void initiateBuild() {
		if (canBuild()){
			Assets.gameInfo.addResources(-Assets.baseComponentData.get(name).getResourceCost());
			if (inProgress)
				queued += 1;
			else {
				inProgress = true;
				timeLeft   = Assets.baseComponentData.get(name).getBuildTime();				
			}
		}
		
	}
	
	public void update(float time) {
		// check buildings 
		if (inProgress){
			timeLeft -= time;
			if (timeLeft<0){
				makeNewElement();
				timeLeft   = 0.0f;
				inProgress = false;
				if (queued > 0){
					queued--;
					inProgress = true;
					timeLeft = Assets.baseComponentData.get(name).getBuildTime();
				}
			}
			
			if (Assets.hasBaseDialog() && Assets.getBaseDialog().getBase() == parent){
				if (canBuild()){
					upDateLabel("Name", name + "(B)");
				}
				upDateLabel("Time", "" + (int) timeLeft);
			
				upDateLabel("Queue","" + queued);
			}
		}
		
		if (Assets.hasBaseDialog() && Assets.getBaseDialog().getBase() == parent &&
			parent.isOwnedByPlayer()){
			upDateLabel("Count","" + getCount());
			if (canBuild()){
				upDateLabel("Name", name + "(B)");
			}
			else
				upDateLabel("Name", name);	
		}
	}
	
	protected abstract void makeNewElement();
	public    abstract int  getCount();
	public    abstract void moveUnit();

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String generateLabel() {
		return name + getCount();
	}

	public void register(String s, Label label) {
		elements.put(s, label);
	}

	public void upDateLabel(String s, String data) {
		if (elements.containsKey(s))
			elements.get(s).setText(data);
	}

	


}