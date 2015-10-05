package com.areacontrol.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameGlobals {
	public static float refreshTime = 0.1f;
	public static Skin skin;
	public static BaseDialog baseDialog = null;
	public static int playerID  = 1;
	public static float resources = 0f;
	public static float resourceRatePerWorker = 10.0f; 
	public static Map<String,BaseComponentData> baseComponentData; 
	public static void registerDialog(BaseDialog bd) {
		if (baseDialog != null){
			baseDialog.reset();
			baseDialog.addAction(Actions.removeActor());
		}
		baseDialog = bd;
	}
	
	static {
		
		baseComponentData = new HashMap<String,BaseComponentData>();
	    	                                  // Cost, Min, Max, Time, Unit?, BuiltBy
		
		BaseComponentData b;
		
		baseComponentData.put("Worker"  ,      new BaseComponentData(  50, 3, 10, 5.0f, false, ""));
		baseComponentData.put("Barracks",  b = new BaseComponentData( 150, 0,  5,10.0f, false, ""));
		b.addUnitBuilt("Marine");
		b.addUnitBuilt("Spy");
		b.addUnitBuilt("Factory");
		
		baseComponentData.put("Factory" ,  b = new BaseComponentData( 150, 0,  5,20.0f, false, ""));
		b.addUnitBuilt("Tank");
		b.addUnitBuilt("AirBase");
		
		baseComponentData.put("AirBase" ,  b = new BaseComponentData( 150, 0,  5,20.0f, false, ""));
		b.addUnitBuilt("Plane");
		
		baseComponentData.put("Research",  b = new BaseComponentData( 300, 0,  1,30.0f, false, ""));
		b.addUnitBuilt("Shields");
		b.addUnitBuilt("Attack");
		//b.addUnitBuilt("Armour");
		//b.addUnitBuilt("Stealth");
		
		baseComponentData.put("Shields",  b = new BaseComponentData( 300, 0,  3, 60.0f, false, "Research"));
		baseComponentData.put("Attack",   b = new BaseComponentData( 300, 0,  3, 60.0f, false, "Research"));
		
		baseComponentData.put("Marine",    new BaseComponentData(  50, 0, 20,10.0f, true, "Barracks"));
		baseComponentData.put("Spy",       new BaseComponentData( 200, 1, 10,10.0f, true, "Barracks"));
		baseComponentData.put("Tank",      new BaseComponentData( 200, 0, 20,10.0f, true, "Factory"));
		baseComponentData.put("Plane",      new BaseComponentData( 200, 0, 20,10.0f, true, "AirBase"));
		
		
	}
	
	
}
