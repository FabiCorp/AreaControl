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
		
		if (Assets.playerID  == parent.getOwner() && 
			Assets.resources > Assets.baseComponentData.get(name).getResourceCost()){
			label += "(B)";
		}
		
		
		if (parent.getOwner() == Assets.playerID){
			
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
		
		if (Assets.baseComponentData.get(name).isUnit()){
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
	
	public void initiateBuild() {
		if (Assets.resources > Assets.baseComponentData.get(name).getResourceCost()){
			Assets.resources -= Assets.baseComponentData.get(name).getResourceCost();
			if (inProgress)
				queued += 1;
			else {
				inProgress = true;
				timeLeft   = Assets.baseComponentData.get(name).getBuildTime();				
			}
		}
		
	}
	
	public void update() {
		if (inProgress){
			timeLeft -= Assets.refreshTime;
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
			
			if (Assets.baseDialog != null && Assets.baseDialog.getBase() == parent){
				if (Assets.resources > Assets.baseComponentData.get(name).getResourceCost()){
					upDateLabel("Name", name + "(B)");
				}
				upDateLabel("Time", "" + (int) timeLeft);
			
				upDateLabel("Queue","" + queued);
			}
		}
		
		if (Assets.baseDialog != null && Assets.baseDialog.getBase() == parent &&
			parent.getOwner() == Assets.playerID){
			upDateLabel("Count","" + getCount());
			if (Assets.resources > Assets.baseComponentData.get(name).getResourceCost()){
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
		try {
			elements.get(s).setText(data);
		} catch (Exception e) {
			System.out.println("Could not Update " + s + " in Component " + name);
		}
		
	}


}