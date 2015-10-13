package com.areacontrol.game;

import appwarp.WarpMessage;

public class ACUnitContainerBaseMessage extends WarpMessage {

	private UnitContainer units;
	
	public ACUnitContainerBaseMessage(UnitContainer units) {
		this.units = units;
	}

	private static final long serialVersionUID = 1L;

	public UnitContainer getUnits() {
		return units;
	}
}
