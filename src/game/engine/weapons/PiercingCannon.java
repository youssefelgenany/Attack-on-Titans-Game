package game.engine.weapons;

import java.util.PriorityQueue;

import game.engine.titans.Titan;

public class PiercingCannon extends Weapon{
	public static final int WEAPON_CODE=1;
	public PiercingCannon(int baseDamage){
		super(baseDamage);
	}
	public int getDamage(){
		return super.getDamage();
	}
	public int getWEAPON_CODE() {
		return WEAPON_CODE;
	}
	public int turnAttack(PriorityQueue<Titan> laneTitans) {
        PriorityQueue<Titan> laneTitansNew = new PriorityQueue<Titan>();
        int resources=0;
        for(int i = 0; i<5 && !laneTitans.isEmpty();i++) {
            resources+=attack(laneTitans.peek());
            if (laneTitans.peek().isDefeated())
                laneTitans.poll();
            else
                laneTitansNew.add(laneTitans.poll());
        }
        laneTitans.addAll(laneTitansNew);
        return resources;
    }
}
