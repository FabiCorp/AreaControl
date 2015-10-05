package com.areacontrol.game;

public class BaseComponentBuildable extends BaseComponent {

	int     count;
	public BaseComponentBuildable(String n,Base parent) {
		super(n,parent);
		
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

	@Override
	protected void makeNewElement() {
		// TODO Auto-generated method stub
		count++;
	}


	@Override
	public void moveUnit() {
		// TODO Auto-generated method stub
		
	}

}
