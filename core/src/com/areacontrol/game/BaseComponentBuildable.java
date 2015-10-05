package com.areacontrol.game;

import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class BaseComponentBuildable extends BaseComponent {

	int     queued; 
	int     count;
	float   timeLeft;
	boolean inProgress;
	
	public BaseComponentBuildable(String n,Base parent) {
		super(n,parent);
		queued       =     0;
		timeLeft     =     0;
		inProgress   = false;
	}
	
	public void initiateBuild() {
		if (GameGlobals.resources > GameGlobals.baseComponentData.get(name).getResourceCost()){
			GameGlobals.resources -= GameGlobals.baseComponentData.get(name).getResourceCost();
			if (inProgress)
				queued += 1;
			else {
				inProgress = true;
				timeLeft   = GameGlobals.baseComponentData.get(name).getBuildTime();				
			}
		}
		
	}
	
	@Override
	public void makeDialog(BaseDialog baseDialog) {
		String label = getName();
		
		if (GameGlobals.playerID  == parent.getOwner() && 
			GameGlobals.resources > GameGlobals.baseComponentData.get(name).getResourceCost()){
			label += "(B)";
		}
		
		TextButton item = new TextButton(label,baseDialog.getSkin());
		item.addListener(new ClickListener() {		
			@Override
			public void clicked(InputEvent event, float x, float y) {
				parent.takeAction(name);
			}
		});
		
		baseDialog.add(item);
		register("Name", item.getLabel());
			
		final Label  rlabel = new Label(" "+(int) timeLeft, baseDialog.getSkin());
		final Label  qlabel = new Label(" "+queued, baseDialog.getSkin());
		
		if (GameGlobals.baseComponentData.get(name).isUnit()){
			TextButton clabel = new TextButton(" "+count, baseDialog.getSkin());
			baseDialog.add(clabel);
			register("Count", clabel.getLabel());
			clabel.addListener(new ClickListener() {		
				@Override
				public void clicked(InputEvent event, float x, float y) {
					parent.moveUnitToSend(name);
				}
			});
		}
		else
		{
			Label  clabel = new Label(" "+count, baseDialog.getSkin());
			baseDialog.add(clabel);
			register("Count", clabel);
		}
				
		baseDialog.add(qlabel);
		register("Queue", qlabel);
		baseDialog.add(rlabel);
		register("Time", rlabel);
		baseDialog.row();
	}
	
	@Override
	public void update() {
		if (inProgress){
			timeLeft -= GameGlobals.refreshTime;
			if (timeLeft<0){
				count     += 1;
				timeLeft   = 0.0f;
				inProgress = false;
				if (queued > 0){
					queued--;
					inProgress = true;
					timeLeft = GameGlobals.baseComponentData.get(name).getBuildTime();
				}
			}
			
			if (GameGlobals.baseDialog != null && GameGlobals.baseDialog.getBase() == parent){
				if (GameGlobals.resources > GameGlobals.baseComponentData.get(name).getResourceCost()){
					upDateLabel("Name", name + "(B)");
				}
				upDateLabel("Time", "" + (int) timeLeft);
			
				upDateLabel("Queue","" + queued);
			}
		}
		
		if (GameGlobals.baseDialog != null && GameGlobals.baseDialog.getBase() == parent &&
			parent.getOwner() == GameGlobals.playerID){
			upDateLabel("Count","" + count);
			if (GameGlobals.resources > GameGlobals.baseComponentData.get(name).getResourceCost()){
				upDateLabel("Name", name + "(B)");
			}
			else
				upDateLabel("Name", name);	
		}
	}
	
	@Override
	public void increaseCount() {
		count += 1;
		System.out.println("Increasing count of" + name + " to " + count);
	}
	
	public void decreaseCount() {
		// TODO Auto-generated method stub
		count--;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

}
