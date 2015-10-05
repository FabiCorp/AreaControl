package com.areacontrol.game;

import java.util.ArrayList;

public class BaseComponentBuildableUnit extends BaseComponentBuildable {

	ArrayList<Unit> units;
	public BaseComponentBuildableUnit(String name, Base parent) {
		super(name, parent);
		// TODO Auto-generated constructor stub
		units = new ArrayList<Unit>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return units.size();
	}

	@Override
	public void increaseCount() {
		// TODO Auto-generated method stub
		units.add(new Unit(name));
	}

	public void addUnit(Unit u){
		units.add(u);
	}
	
	public Unit removeUnit() {
		// TODO Auto-generated method stub
		
		try
		{
			return units.remove(0);
		} catch  (IndexOutOfBoundsException e) {
		    System.err.println("IndexOutOfBoundsException: " + e.getMessage()); 
		} 
		return null;
	}

}
