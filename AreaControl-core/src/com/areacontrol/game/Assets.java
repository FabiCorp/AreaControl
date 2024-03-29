package com.areacontrol.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Assets {
	

	public static Skin skin;
	
	public static int   keyP   = 44;
	public static int   keyQ   = 45;
	public static int   keyESC = 131;
	
	public static int   singlePlayerMode = 1;
	public static int   multiPlayerMode  = 2;
	
	// public static boolean singleUnitMessages = true; 
	
	//public static float refreshTime = 0.1f;
	
	private static BaseDialog baseDialog = null;
	public static  GameInfoData gameInfo = new GameInfoData();
	
	public static float resourceRatePerWorker = 10.0f; 
	public static Map<String,BaseComponentData> baseComponentData;

	public static float fightTime = 0.5f;
	public static void registerDialog(BaseDialog bd) {
		if (baseDialog != null){
			baseDialog.reset();
			baseDialog.addAction(Actions.removeActor());
		}
		baseDialog = bd;
	}
	
	static void setBuildTimeZero(){
		for(Map.Entry<String,BaseComponentData> e: baseComponentData.entrySet()){
			e.getValue().setBuildTime(0.0f);
		}
		
	}
	
	public static Logger log =  Logger.getLogger("com.areacontrol.game");
	
	static {
		
		baseComponentData = new HashMap<String,BaseComponentData>();
	    	                                  // Cost, Start#, Max#, Time, Unit?, BuiltBy
		
		BaseComponentData b;
		
		baseComponentData.put("Worker"  ,      new BaseComponentData(  50, 3, 10, 5.0f, false, "", 100));
		baseComponentData.put("Barracks",  b = new BaseComponentData( 150, 1,  5,10.0f, false, "", 100));
		b.addUnitBuilt("Marine");
		b.addUnitBuilt("Spy");
		b.addUnitBuilt("Factory");
		
		baseComponentData.put("Factory" ,  b = new BaseComponentData( 150, 0,  5,20.0f, false, "",100));
		b.addUnitBuilt("Tank");
		b.addUnitBuilt("AirBase");
		
		baseComponentData.put("AirBase" ,  b = new BaseComponentData( 150, 0,  5,20.0f, false, "",100));
		b.addUnitBuilt("Bomber");
		
		baseComponentData.put("Research",  b = new BaseComponentData( 300, 0,  1,30.0f, false, "",100));
		b.addUnitBuilt("Shields");
		b.addUnitBuilt("Attack");
		//b.addUnitBuilt("Armour");
		//b.addUnitBuilt("Stealth");
		
		baseComponentData.put("Shields",  b = new BaseComponentData( 300, 0,  3, 60.0f, false, "Research",0));
		baseComponentData.put("Attack",   b = new BaseComponentData( 300, 0,  3, 60.0f, false, "Research",0));
		
		baseComponentData.put("Marine",    new BaseComponentData(  50, 0, 20,10.0f, true, "Barracks",20));
		baseComponentData.put("Spy",       new BaseComponentData( 200, 1, 10,10.0f, true, "Barracks",10));
		baseComponentData.put("Tank",      new BaseComponentData( 200, 0, 20,10.0f, true, "Factory", 50));
		baseComponentData.put("Bomber",    new BaseComponentData( 200, 0, 20,10.0f, true, "AirBase",100));
		
		
	}

	public static BaseDialog getBaseDialog() {
		if (baseDialog != null)
			return baseDialog;
		else
			throw new IllegalStateException();
	}

	public static boolean hasBaseDialog() {
		return baseDialog != null;
	}
	
	public static Random randGen = new Random();

	public static int BaseCount = 0;
}
