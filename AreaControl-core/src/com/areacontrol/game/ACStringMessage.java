package com.areacontrol.game;

import appwarp.SerializableWarpMessage;


public class ACStringMessage extends SerializableWarpMessage {

	private static final long serialVersionUID = 1L;
	String s;
	
	public ACStringMessage(String s) {
		this.s=s;
	}
	
	public String getString(){
		return s;
	}
	
}
