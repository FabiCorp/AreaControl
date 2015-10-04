package com.areacontrol.game;

public class BaseComponent {
	private String name;
	private int    count;
	private float  timeLeft;
	private float  buildTime;
	private int    resourceCost;
	public BaseComponent(String n) {
		name         =     n;
		count        =     0;
		timeLeft     =     0; 
		buildTime    = 20.0f;
		resourceCost =  2000;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void increaseCount(){
		count += 1;
	}
	public String generateLabel(){
		return name + count;
	}
}
