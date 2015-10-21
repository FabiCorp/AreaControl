package com.areacontrol.game;

import appwarp.SerializableWarpMessage;

public class ACUnitMessage extends SerializableWarpMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Unit unit;
	
	public ACUnitMessage(Unit unit) {
		this.unit = unit;
	}
	
	public Unit getUnit(){
		return unit;
	}
}
