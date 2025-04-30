package game.engine.interfaces;

public interface Attacker {
	
	 int getDamage();
	 default int attack(Attackee target) {
		 int x =getDamage();
		 return target.takeDamage(x);
	 }
}
