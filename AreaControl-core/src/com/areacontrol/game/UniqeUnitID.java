package com.areacontrol.game;

import java.io.Serializable;

public class UniqeUnitID implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int unitIdCounter = 0;
	protected int playerID;
	protected int unitID;
	public UniqeUnitID(int playerID){
		this.playerID = playerID;
		unitID        = unitIdCounter++;
	}
	
	public int getPlayerId() {
		return playerID;
	}
	
}
