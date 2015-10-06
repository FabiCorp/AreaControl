package com.areacontrol.game;

import java.util.ArrayList;


public class UnitContainer {
	ArrayList<Unit> units;
	
	public UnitContainer(){
		units = new ArrayList<Unit>();
	}
	
	public void addUnits(Unit bc){
		units.add(bc);
	}

}
