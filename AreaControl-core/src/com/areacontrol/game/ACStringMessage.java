package com.areacontrol.game;

import appwarp.WarpMessage;

public class ACStringMessage extends WarpMessage {

	private static final long serialVersionUID = 1L;
	String s;
	
	public ACStringMessage(String s) {
		this.s=s;
	}
	
	public String getString(){
		return s;
	}
	
}
