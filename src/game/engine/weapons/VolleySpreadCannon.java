package game.engine.weapons;

import java.util.PriorityQueue;

import game.engine.titans.Titan;

public class VolleySpreadCannon extends Weapon{
	public static final int WEAPON_CODE=3;
	private final int minRange;
	private final int maxRange;
	public VolleySpreadCannon(int baseDamage, int minRange, int maxRange){
		super(baseDamage);
		this.minRange = minRange;
		this.maxRange = maxRange;
	}
	
	public int getWEAPON_CODE() {
		return WEAPON_CODE;
	}
	public int getMinRange() {
		return minRange;
	}
	public int getMaxRange() {
		return maxRange;
	}
	public int getDamage(){
		return super.getDamage();
	}
	public int turnAttack(PriorityQueue<Titan> laneTitans) {
		int resources=0;
		PriorityQueue<Titan> laneTitansNew = new PriorityQueue<Titan>();
		while(!laneTitans.isEmpty()) {
			if(laneTitans.peek().getDistance()>=minRange && laneTitans.peek().getDistance()<=maxRange ) {
				resources+=attack(laneTitans.peek());
				if (laneTitans.peek().isDefeated()) {
					laneTitans.poll();
				}
				else
					laneTitansNew.add(laneTitans.poll());
			}
			else 
				laneTitansNew.add(laneTitans.poll());
		}
		laneTitans.addAll(laneTitansNew);
		return resources;
	}
}
