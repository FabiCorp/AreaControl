package com.areacontrol.game;

import java.io.Serializable;

public enum ACUnitContainerMessageType implements Serializable {
	PreFightFromAttacker,PreFightFromDefender,InFight,Postfight,Undefined;
}
