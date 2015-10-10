package com.areacontrol.game;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import appwarp.WarpController;
import appwarp.WarpMessage;

public class Assets {
	public static Skin skin;
	
	public static int   keyP   = 44;
	public static int   keyQ   = 45;
	public static int   keyESC = 131;
	
	public static int   singlePlayerMode = 1;
	public static int   multiPlayerMode  = 2;
	
	//public static float refreshTime = 0.1f;
	
	private static BaseDialog baseDialog = null;
	public static int playerID  = 1;
	public static float resources = 0f;
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
	
	static {
		
		baseComponentData = new HashMap<String,BaseComponentData>();
	    	                                  // Cost, Min, Max, Time, Unit?, BuiltBy
		
		BaseComponentData b;
		
		baseComponentData.put("Worker"  ,      new BaseComponentData(  50, 3, 10, 5.0f, false, ""));
		baseComponentData.put("Barracks",  b = new BaseComponentData( 150, 1,  5,10.0f, false, ""));
		b.addUnitBuilt("Marine");
		b.addUnitBuilt("Spy");
		b.addUnitBuilt("Factory");
		
		baseComponentData.put("Factory" ,  b = new BaseComponentData( 150, 0,  5,20.0f, false, ""));
		b.addUnitBuilt("Tank");
		b.addUnitBuilt("AirBase");
		
		baseComponentData.put("AirBase" ,  b = new BaseComponentData( 150, 0,  5,20.0f, false, ""));
		b.addUnitBuilt("Bomber");
		
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
		baseComponentData.put("Bomber",    new BaseComponentData( 200, 0, 20,10.0f, true, "AirBase"));
		
		
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
}
