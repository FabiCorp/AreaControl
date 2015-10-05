package com.areacontrol.game;

import java.util.HashMap;
import java.util.Map;

public class BaseComponentContainer {
	Map<String,BaseComponent> component;
	boolean activated;
	public BaseComponentContainer(){
		component = new HashMap<String,BaseComponent>();
		activated = false;
	}
	
	public void addUnits(BaseComponent bc){
		component.put(bc.getName(),bc);
	}

	public void activate() {
		activated = true;
	}

	public boolean isActivated() {
		// TODO Auto-generated method stub
		return activated;
	}
	
}
