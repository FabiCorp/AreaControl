package com.areacontrol.game;

import java.util.ArrayList;

public class BaseComponentData {
	private int     resourceCost;
	private int     minPerBase;   // only occupied bases
	private int     maxPerBase;
	private float   buildTime;
	private boolean isUnit;
	private String  builtBy;      // the building which makes the unit or building 
	
	ArrayList<String> builds;
	public BaseComponentData(int resourceCost,int minPerBase, int maxPerBase, float buildTime,boolean isUnit,String builtBy){
		this.resourceCost = resourceCost;
		this.maxPerBase   = maxPerBase;
		this.minPerBase   = minPerBase;
		this.buildTime    = buildTime;
		this.isUnit  	  = isUnit;
		this.builtBy      = builtBy;
		
		builds = new ArrayList<String>();
	}

	public void addUnitBuilt(String s){
		builds.add(s);
	}
	
	public ArrayList<String> enables(){
		return builds;
	}
	
	public int getResourceCost() {
		return resourceCost;
	}

	public float getBuildTime() {
		return buildTime;
	}

	public boolean isUnit() {
		return isUnit;
	}

	public String getBuiltBy() {
		return builtBy;
	}

	public int getMinPerBase() {
		return minPerBase;
	}

	public int getMaxPerBase() {
		return maxPerBase;
	}



}
