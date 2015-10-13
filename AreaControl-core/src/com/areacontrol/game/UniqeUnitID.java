package com.areacontrol.game;

public class UniqeUnitID {
	static int unitIdCounter = 0;
	protected int playerID;
	protected int unitID;
	public UniqeUnitID(int playerID){
		this.playerID = playerID;
		unitID        = unitIdCounter++;
	}
	
	
}
