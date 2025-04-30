package game.engine.weapons;

import java.util.PriorityQueue;

import game.engine.titans.Titan;

public class SniperCannon extends Weapon{
	public int getWEAPON_CODE() {
		return WEAPON_CODE;
	}
	public static final int WEAPON_CODE=2;
	public SniperCannon(int baseDamage){
		super(baseDamage);
	}
	public int getDamage(){
		return super.getDamage();
	}
	public int turnAttack(PriorityQueue<Titan> laneTitans) {
		if (laneTitans.isEmpty())
			return 0;
		int resources=attack(laneTitans.peek());
		if (laneTitans.peek().isDefeated())
			laneTitans.poll();
		return resources;
	}

}
