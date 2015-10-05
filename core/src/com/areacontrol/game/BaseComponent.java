package com.areacontrol.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class BaseComponent {

	protected String name;
	int              count;
	protected Base parent;
	protected Map<String,Label> elements;
	
	public BaseComponent(String name, Base parent) {
		this.name   = name;
		this.parent = parent;
		elements    = new HashMap<String,Label>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void increaseCount() {
		count += 1;
		System.out.println("Increasing count of" + name + " to " + count);
	}

	public String generateLabel() {
		return name + count;
	}

	public void register(String s, Label label) {
		elements.put(s, label);
	}

	public void upDateLabel(String s, String data) {
		elements.get(s).setText(data);
	}

	public void makeDialog(BaseDialog baseDialog) {
		String label = getName();
	
		Label nameElement = new Label(label,baseDialog.getSkin());
		baseDialog.add(nameElement);
		register("Name", nameElement);
		
		if (GameGlobals.baseComponentData.get(name).isUnit()){
			TextButton clabel = new TextButton(" "+count, baseDialog.getSkin());
			baseDialog.add(clabel);
			register("Count", clabel.getLabel());
			clabel.addListener(new ClickListener() {		
				@Override
				public void clicked(InputEvent event, float x, float y) {
					parent.moveUnitFromSend(name);
				}
			});
		}
		else
		{
			Label  clabel = new Label(" "+count, baseDialog.getSkin());
			baseDialog.add(clabel);
			register("Count", clabel);
		}
				
		baseDialog.row();
	}

	
	public void update() {
	
		if (GameGlobals.baseDialog != null && GameGlobals.baseDialog.getBase() == parent &&
			parent.getOwner() == GameGlobals.playerID){
			upDateLabel("Count","" + count);
		}
	}

	public int getCount() {
		return count;
	}
	
	public void decreaseCount() {
		// TODO Auto-generated method stub
		count--;
	}

}