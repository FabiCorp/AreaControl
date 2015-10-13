package com.areacontrol.game;

public class GameInfoData {
	private int    playerID;
	private float  resources;
	
	public GameInfoData() {
		playerID = 1;
	}
	public int getPlayerID() {
		return playerID;
	}
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	public float getResources() {
		return resources;
	}
	public void setResources(float resources) {
		this.resources = resources;
	}
	public void addResources(float f) {
		resources += f;
	}
	
	
}
