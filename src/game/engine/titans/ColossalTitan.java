package game.engine.titans;

import game.engine.interfaces.Attackee;
import game.engine.interfaces.Attacker;
import game.engine.interfaces.Mobil;

public class ColossalTitan extends Titan implements Attacker, Attackee, Mobil, Comparable<Titan>{
	public int getTITAN_CODE() {
		return TITAN_CODE;
	}
	public static final int TITAN_CODE = 4;
	public ColossalTitan(int baseHealth, int baseDamage, int heightInMeters, int distanceFromBase,
			int speed, int resourcesValue, int dangerLevel){
		super(baseHealth, baseDamage, heightInMeters, distanceFromBase,
				speed, resourcesValue, dangerLevel);		
	}
	public boolean move() {
		setDistance(getDistance()-getSpeed());
		setSpeed(getSpeed()+1);
		if (hasReachedTarget())
			return true;
		return false;
	}

}
