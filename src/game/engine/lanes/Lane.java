package game.engine.lanes;
import game.engine.weapons.Weapon;
import game.engine.titans.Titan;
import game.engine.base.Wall;
import java.util.*;

public class Lane implements Comparable<Lane> {
	private final Wall laneWall;
	private int dangerLevel;
	private final PriorityQueue<Titan> titans;
	private final ArrayList<Weapon> weapons;
	
	public Lane(Wall laneWall){
		this.laneWall = laneWall;
		this.titans = new PriorityQueue<Titan>();
		this.dangerLevel=0;
		this.weapons= new ArrayList<Weapon>();
	}
	public int compareTo(Lane o){
		return this.dangerLevel-o.dangerLevel;
	}
	public Wall getLaneWall() {
		return laneWall;
	}
	public int getDangerLevel() {
		return dangerLevel;
	}
	public void setDangerLevel(int dangerLevel) {
		this.dangerLevel=dangerLevel;
	}
	public PriorityQueue<Titan> getTitans() {
		return titans;
	}
	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}
	
	public void addTitan(Titan titan) {
			titans.add(titan);
	}
	public void addWeapon(Weapon weapon) {
		weapons.add(weapon);
	}
	public void moveLaneTitans() {
		PriorityQueue<Titan> titansNew = new PriorityQueue<Titan>();
		while (!titans.isEmpty()) {
			if(!titans.peek().hasReachedTarget()) {
				titans.peek().move();
				titansNew.add(titans.poll());
			}
			else
				titansNew.add(titans.poll());
		}
		titans.addAll(titansNew);
	}
	public int performLaneTitansAttacks() {
		int resources =0;
		for (Titan titan : titans) {
			if(titan.hasReachedTarget()) {
				resources+=titan.attack(this.laneWall);
			}
		}
		return resources;
	}
	public int performLaneWeaponsAttacks() {
		int resources =0;
		for (int i=0;i<weapons.size();i++) {
					resources+=weapons.get(i).turnAttack(titans);
		}
		return resources;
	}
	public boolean isLaneLost() {
		if (laneWall.isDefeated())
			return true;
		else {
			return false;
		}
	}
	public void updateLaneDangerLevel() {
		int danger =0;
		for (Titan titan : titans) {
			danger+=titan.getDangerLevel();
		}
		setDangerLevel(danger);
	}
}
