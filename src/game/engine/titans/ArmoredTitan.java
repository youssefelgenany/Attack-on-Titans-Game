package game.engine.titans;

import game.engine.interfaces.Attackee;
import game.engine.interfaces.Attacker;
import game.engine.interfaces.Mobil;

public class ArmoredTitan extends Titan implements Attacker, Attackee, Mobil, Comparable<Titan>{
	public int getTITAN_CODE() {
		return TITAN_CODE;
	}
	public static final int TITAN_CODE = 3;
	public ArmoredTitan(int baseHealth, int baseDamage, int heightInMeters, int distanceFromBase,
			int speed, int resourcesValue, int dangerLevel){
		super(baseHealth, baseDamage, heightInMeters, distanceFromBase,
				speed, resourcesValue, dangerLevel);		
	}
	public  int takeDamage(int damage) {
		int x=getCurrentHealth()- damage/4;
		setCurrentHealth(x);
		if(isDefeated()) {
			return getResourcesValue();
		}
		else
			return 0;
	}

}
