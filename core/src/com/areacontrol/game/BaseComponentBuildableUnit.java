package com.areacontrol.game;

import java.util.ArrayList;

public class BaseComponentBuildableUnit extends BaseComponent {

	ArrayList<Unit>    units;
	BaseComponentUnit  unitStore;
	

	public BaseComponentBuildableUnit(String name, Base parent) {
		super(name, parent);
		units     = new ArrayList<Unit>();
		unitStore = new BaseComponentUnit(name,parent,this); 
	}

	@Override
	public int getCount() {
		return units.size();
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

	@Override
	protected void makeNewElement() {
		// TODO Auto-generated method stub
		units.add(new Unit(name));
	}

	@Override
	public void moveUnit() {
		if (units.size()>0){
			Unit u = units.remove(0);
			unitStore.addUnit(u);
		}
	}

	public BaseComponentUnit getUnitStore() {
		return unitStore;
	}
}
